package com.rakursy.timetable.model;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.collection.LambdaCollections.*;
import static org.hamcrest.Matchers.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Grade implements Serializable {

	private static final long serialVersionUID = 9060597511240233472L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@NotBlank
	private String name;

	@NotNull
	@OneToMany(cascade = { CascadeType.ALL })
	private List<GradeSubject> subjects;

	@NotNull
	@OneToMany(cascade = { CascadeType.ALL })
	private List<StudentGroup> studentGroups;

	@NotNull
	@Min(value = 1)
	private Integer studentCount;

	@OneToOne
	private Room classRoom;

	@NotNull
	@ManyToOne
	private School school;

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(name)
				.append(studentCount)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Grade) {
			Grade other = (Grade) obj;
			return new EqualsBuilder()
					.append(id, other.id)
					.append(name, other.name)
					.append(studentCount, other.studentCount)
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return name;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GradeSubject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<GradeSubject> subjects) {
		this.subjects = subjects;
	}

	public void addSubject(Subject subject, List<Teacher> teachers, Integer classCountPerWeek) {
		if (subjects == null) {
			subjects = new ArrayList<GradeSubject>();
		}

		this.subjects.add(new GradeSubject(subject, teachers, classCountPerWeek));
	}

	@Transient
	public List<Subject> getSubjectList() {
		return with(subjects).extract(on(GradeSubject.class).getSubject());
	}

	@Transient
	public List<Teacher> getTeachersForSubject(Subject subject) {
//		return with(subjects).retain(having(on(GradeSubject.class).getSubject(), equalTo(subject))).extract(forEach(on(GradeSubject.class).getTeachers()));
		for (GradeSubject gradeSubject : subjects) {
			if (gradeSubject.getSubject().equals(subject)) {
				return gradeSubject.getTeachers();
			}
		}
		return null;
	}

	@Transient
	public Integer getClassCountForSubject(Subject subject) {
		for (GradeSubject gradeSubject : subjects) {
			if (gradeSubject.getSubject().equals(subject)) {
				List<StudentGroup> groups = select(studentGroups, having(on(StudentGroup.class).getSubject(), equalTo(subject)));
				return gradeSubject.getClassCountPerWeek() * groups.size();
			}
		}
		return null;
	}

	public Integer getClassCountForSubjectWithTeacher(Subject subject,
			Teacher teacher) {
		for (GradeSubject gradeSubject : subjects) {
			if (gradeSubject.getSubject().equals(subject)) {
				List<StudentGroup> groups = select(studentGroups, 
						having(on(StudentGroup.class).getSubject(), equalTo(subject))
						.and(having(on(StudentGroup.class).getTeacher(), equalTo(teacher))));
				return gradeSubject.getClassCountPerWeek() * groups.size();
			}
		}
		return null;
	}

	@Transient
	public Integer getTotalClassCountPerWeek() {
		Integer totalClassCount = 0;
		for (GradeSubject gradeSubject : subjects) {
			totalClassCount += getClassCountForSubject(gradeSubject.getSubject());
		}
		return totalClassCount;
	}

	public List<StudentGroup> getStudentGroups() {
		return studentGroups;
	}

	public void setStudentGroups(List<StudentGroup> studentGroups) {
		this.studentGroups = studentGroups;
	}

	public void addStudentGroup(StudentGroup studentGroup) {
		if (studentGroups == null) {
			studentGroups = new ArrayList<StudentGroup>();
		}

		this.studentGroups.add(studentGroup);
	}

	public Integer getStudentCount() {
		return this.studentCount;
	}

	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}

	public Room getClassRoom() {
		return this.classRoom;
	}

	public void setClassRoom(Room classRoom) {
		this.classRoom = classRoom;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

}
