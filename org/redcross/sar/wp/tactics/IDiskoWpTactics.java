package org.redcross.sar.wp.tactics;

import org.redcross.sar.wp.IDiskoWp;

public interface IDiskoWpTactics extends IDiskoWp {
	
	public enum TacticsTaskType {
		ELEMENT_TASK,
		LIST_TASK,
		MISSON_TASK,
		HYPOTHESIS_TASK,
		PRIORITY_TASK,
		REQUIREMENT_TASK,
		DESCRIPTION_TASK,
		UNIT_TASK,
		MAKE_READY_TASK,
		PRINT_TASK
    }
}