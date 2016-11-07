package com.kawakp.demingliu.jinandemo.tree.bean;

public class Bean
{
	@TreeNodeId
	private String id;
	@TreeNodePid
	private String pId;
	@TreeNodeLabel
	private String label;

	@TreeNodeModleId
	private String plcDataModelId;

	@TreeNodeDeviceId
	private String deviceId;

	@TreeNodeDevideStatus
	private String status;

	public Bean()
	{
	}

	public Bean(String id, String pId, String label,String plcDataModelId,String deviceId ,String status)
	{
		this.id = id;
		this.pId = pId;
		this.label = label;
		this.plcDataModelId = plcDataModelId;
		this.deviceId = deviceId;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	public String getPlcDataModelId() {
		return plcDataModelId;
	}

	public void setPlcDataModelId(String plcDataModelId) {
		this.plcDataModelId = plcDataModelId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
