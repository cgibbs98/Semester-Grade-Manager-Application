package application;

public class categoryobject {

	private String name;
	private String weight;
	private String filepath;
	
	public categoryobject(String name, String weight, String filepath) { 
		this.name = name; 
		this.weight = weight; 
		this.filepath = filepath; 
	} 
	
	public String getName() {
		return name;
	}
	
	public String getWeight() {
	    return weight;
	}
	
	public String getFilepath() {
	    return filepath;
	}
	
	public void setName(String value) {
		this.name = value;
	}
	
	public void setWeight(String value) {
		this.weight = value;
	}
	
	public void setFilepath(String value) {
		this.filepath = value;
	}
	
}//End of category object
