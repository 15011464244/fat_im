/**
 * 
 */
package com.newcdc.model;

import java.util.List;

/**
 * @author zhangfan  2015-4-1,下午8:40:59
 * 
 */
public class QueryTaskBean {


	private List<String> taskNums;

	public List<String> getTaskNums() {
		return taskNums;
	}
	public void setTaskNums(List<String> taskNums) {
		this.taskNums = taskNums;
	}
	@Override
	public String toString() {
		return "QueryTaskBean [taskNums=" + taskNums + "]";
	}
}
