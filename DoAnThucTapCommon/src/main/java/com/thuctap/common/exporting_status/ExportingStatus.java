package com.thuctap.common.exporting_status;
import jakarta.persistence.*;

@Entity
@Table(name = "exporting_status")
public class ExportingStatus {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column(nullable = false, length = 125)
	    private String name;

	    @Column(length = 225)
	    private String description;

		public ExportingStatus() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		

		public ExportingStatus(Integer id) {
			super();
			this.id = id;
		}



		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	    
	    
	
}
