# JMA
Job Management API

# Flexibility
Achieved by providing a simple interface com.optile.jma.api.ICommand to implemented by service's client, which enables client customize possible actions, by providing execution and rollback logic.

# Reliability
If one task failed to execute, whatever it failed due to execution, or interruption, or it exceeded timeout constriants, the whole already executed tasks before will be rolledback through undo method of com.optile.jma.api.ICommand.

# Internal Consistency
Achieved by using synchronized logic for updating job status.

# Priority
Achieved by using concurrent ordered queuue.

#Scheduling
by providing simple API com.optile.jma.config.apis.IJobsQueue to manage multiple instances of jobs.
- You can run job immediately through enqueue(job) method. The job will be executed immediately if the queue is empty or there is an idle thread if IJobsQueue is configured with allowsParallelRuns flag equals true, or usage of priority ordering strategy while ordering jobs, with Max priority allowance.

- You can schedule the job using schedule(job, scheduleConfig) method.

#Configuration Options
package com.optile.jma.config.apis providing APIS for configuring queues, jobs, tasks, schedules.

#Improvements
- Developing deterministic tests, "Without Thread.sleep()", and enhance design if required.
- Providing JobsManager API for more advanced management, managing queues.
 