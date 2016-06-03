package cn.edu.cylg.cis.hicloud.core.plugins.tree;

public class TreeNode {  
    private String id;  
    private String pId;  
    private String name;  
    private String idt;
    private Boolean checked;  
    private Boolean open;
    private Boolean nocheck;
    private Boolean isParent;
    
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdt() {
		return idt;
	}

	public void setIdt(String idt) {
		this.idt = idt;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}
	
   
    public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public Boolean getNocheck() {
		return nocheck;
	}

	public void setNocheck(Boolean nocheck) {
		this.nocheck = nocheck;
	}

	public TreeNode(String id, String pId, String name,
			Boolean checked, Boolean open, String idt) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.idt = idt;
		this.checked = checked;
		this.open = open;
	}
	
	
	

	public TreeNode(String id, String pId, String name,Boolean nocheck, 
			Boolean open,Boolean isParent) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.nocheck = nocheck;
		this.open = open;
		this.isParent = isParent;
	}

	public TreeNode() {  
        super();  
    }  
      
  
}  