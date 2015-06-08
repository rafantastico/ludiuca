/*******************************************************************************
 * Ludiuca
 *
 * Copyright (C) (c) 2015 Ludiuca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package es.ugr.rodgom.ludiuca.client;

import java.io.Serializable;

public class Task implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String description;
	private String date;
	private int courseID;
	private String awardType;
	private int value;
	
	public Task(){
		
	}
	
	public Task(int id,String name, String description, String date, int courseID, String awardType, int value){
		this.id = id;
		this.name = name;
		this.description = description;
		this.date = date;
		this.courseID = courseID;
		this.awardType = awardType;
		this.value = value;
	}
	
	public Task(String name, String description, String date, int courseID, String awardType, int value){
		this.name = name;
		this.description = description;
		this.date = date;
		this.courseID = courseID;
		this.awardType = awardType;
		this.value = value;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getCourseID() {
		return courseID;
	}
	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}
	public String getAwardType() {
		return awardType;
	}
	public void setAwardType(String awardType) {
		this.awardType = awardType;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStringValue() {
		String valueStr = "";
		switch(this.value){
			case 1:
				valueStr = "Oro";
				break;
			case 2:
				valueStr = "Plata";
				break;
			case 3:
				valueStr = "Bronce";
				break;
		}
		return valueStr;
	}
}
