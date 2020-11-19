package application;

public class CategoryObject {

	private String name;
	private String weight;
	private String filePath;
	
	public CategoryObject(String name, String weight, String filePath) { 
		this.name = name; 
		this.weight = weight; 
		this.filePath = filePath; 
	} 
	
	public String getName() {
		return name;
	}
	
	public String getWeight() {
	    return weight;
	}
	
	public String getFilePath() {
	    return filePath;
	}
	
	public void setName(String value) {
		this.name = value;
	}
	
	public void setWeight(String value) {
		this.weight = value;
	}
	
	public void setFilePath(String value) {
		this.filePath = value;
	}
	
}//End of CategoryObject
