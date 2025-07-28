package com.thuctap.common.setting;
import jakarta.persistence.*;

@Entity
@Table(name = "settings")
public class Setting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "`key`", nullable = false, unique = true, length = 65)
	private String key;

	@Column(name = "`value`", nullable = false, length = 500)
	private String value;

	@Column(name = "type", nullable = false, length = 65)
	private String type;

	public Setting() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
