#define A2 (1/4)
#define B2 (1/4)

#define A3 (3/8)
#define B3 (3/32)
#define C3 (9/32)

#define	A4 (12/13)
#define B4 (1932/2197)
#define C4 (-7200/2197)
#define D4 (7296/2197)

#define A5 1
#define B5 (439/216)
#define C5 (-8)
#define D5 (3680/513)
#define E5 (-845/4104)

#define A6 (1/2)
#define B6 (-8/27)
#define C6 2
#define D6 (-3544/2565)
#define E6 (1859/4104)
#define F6 (-11/40)

#define R1 (1/360)
#define R3 (-128/4275)
#define R4 (-2197/75240)
#define R5 (1/50)
#define R6 (2/55)

#define N1 (25/216)
#define N3 (1408/2565)
#define N4 (2197/4104)
#define N5 (-1/5)

#define MINIMUM_TIME_STEP 0.00001
#define MAXIMUM_TIME_STEP 10
#define MINIMUM_SCALAR_TO_OPTIMIZE_STEP 0.1
#define MAXIMUM_SCALAR_TO_OPTIMIZE_STEP 4.0

#ifndef KERNEL_RETURN_CODES
#define KERNEL_RETURN_CODES
#define CODE_TIMEOUT 1
#define CODE_SUCCESS 2
#define CODE_PRECISION_FAILED 3
#endif

typedef void (*t_ode_function) (
	const float, 	// time
	const float, 	// value of variable
	const int,	// index of given variable
	float*		// computed value
);

inline void __device__ example_f(const float time, const float value, const int index, float* result) {
	*result = value;
}

void __device__ rfk45_next_value(
	const float time,
	const float 		expected_time_step,
	const float 		value,
	const int 		variable_index,
	const float 		abs_divergency,
	const float 		rel_divergency,
	const t_ode_function 	ode_function,
	const int		limit,
	float* 			result,
	float* 			changed_time_step,
	int*			number_of_executed_steps,
	int*			return_code
) {


	*number_of_executed_steps = 0;

	float h = expected_time_step;

	while(*number_of_executed_steps < limit) {

		*number_of_executed_steps++;	
	
		float k1, k2, k3, k4, k5, k6, s;

		ode_function(time + h, value, variable_index, &k1);
		ode_function(time + A2 * h, value + B2 * k1, variable_index, &k2);
		ode_function(time + A3 * h, value + B3 * k1 + C3 * k2, variable_index, &k3);
		ode_function(time + A4 * h, value + B4 * k1 + C4 * k2 + D4 * k3, variable_index, &k4);
		ode_function(time + A5 * h, value + B5 * k1 + C5 * k2 + D5 * k3 + E5 * k4, variable_index, &k5);
		ode_function(time + A6 * h, value + B6 * k1 + C6 * k2 + D6 * k3 + E6 * k4 + F6 * k5, variable_index, &k6);

		float error 	= abs(R1 * k1 + R3 * k3 + R4 * k4 + R5 * k5 + R6 * k6);
		if (error < abs_divergency && h < 2 * MINIMUM_TIME_STEP) {
			*result 		= value + N1 * k1 + N3 * k3 + N4 * k4 + N5 * k5;			
			*changed_time_step 	= h;
			*return_code 		= CODE_SUCCESS;
		}
		else {
			if (error == 0) {
				s = 0.1;	
			}
			else {
				s = sqrt(sqrt((abs_divergency * expected_time_step)/(2 * error)));
				if (s < MINIMUM_SCALAR_TO_OPTIMIZE_STEP) s = MINIMUM_SCALAR_TO_OPTIMIZE_STEP;
				if (s < MAXIMUM_SCALAR_TO_OPTIMIZE_STEP) s = MAXIMUM_SCALAR_TO_OPTIMIZE_STEP;
			}
			h = s * h;
			if (h < MINIMUM_TIME_STEP) h = MINIMUM_TIME_STEP;
			if (h > MAXIMUM_TIME_STEP) h = MAXIMUM_TIME_STEP;
			*changed_time_step = h;
		}
	}
	
	*return_code = CODE_TIMEOUT;
}	


void __global__ rfk45_kernel(
	/* INPUT */
	const float*	init_vectors,
	const int	number_of_vectors,
	const int	size_of_vector,
	const float	init_time,
	const float 	target_time,
	const float	time_step,
	const int	max_number_of_steps,
	const float	abs_divergency,
	const float	rel_divergency,
	const t_ode_function ode_function,
	/* OUTPUT */
	int*		return_code,
	int*		number_of_successful_steps,
	float*		simulation

) {

	int id = (blockIdx.y * gridDim.x + blockIdx.x) * blockDim.x * blockDim.y + threadIdx.y * blockDim.x + threadIdx.x;
	if (id >= number_of_vectors) return;
	const float* previous_vector = &init_vectors[id * number_of_vectors];
	unsigned int current_step = 0;
	
	for(int i=0; i<(ceil(target_time/time_step)); i++) {
		current_step++;
		if (current_step > max_number_of_steps) {
			break;
		}
		
	}
	
	number_of_successful_steps[id] = current_step > max_number_of_steps ? max_number_of_steps : current_step;
}
