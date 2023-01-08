package emlakcepte.dto.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import emlakcepte.model.Realty;
import emlakcepte.model.enums.UserType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class UserResponse {
	private String name;
	private String email;
	private UserType type;

	private List<Realty> realtyList;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime createDate;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime packageExpireDate;

	@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
	public LocalDateTime getCreateDate()
	{
		return createDate;
	}

	@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
	public void setCreateDate(LocalDateTime createDate)
	{
		this.createDate = createDate;
	}

	@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
	public LocalDateTime getPackageExpireDate()
	{
		return packageExpireDate;
	}

	@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
	public void setPackageExpireDate(LocalDateTime packageExpireDate)
	{
		this.packageExpireDate = packageExpireDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public List<Realty> getRealtyList()
	{
		return realtyList;
	}

	public void setRealtyList(List<Realty> realtyList)
	{
		this.realtyList = realtyList;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserResponse that = (UserResponse) o;
		return Objects.equals(name, that.name) && Objects.equals(email, that.email) && type == that.type && Objects.equals(realtyList, that.realtyList) && Objects.equals(createDate, that.createDate) && Objects.equals(packageExpireDate, that.packageExpireDate);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name, email, type, realtyList, createDate, packageExpireDate);
	}

	@Override
	public String toString() {
		return "UserResponse [name=" + name + ", email=" + email + ", type=" + type + "]";
	}

}
