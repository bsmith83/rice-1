/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kim.bo;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.kuali.core.bo.PersistableBusinessObjectBase;
import org.kuali.core.util.TypedArrayList;

public class Group extends PersistableBusinessObjectBase {

	private static final long serialVersionUID = 4974576362491778342L;

	private Long id;
	private String name;
	private String description;
	private ArrayList<Group> memberGroups;
	private ArrayList<Group> parentGroups;
    private ArrayList<Role> groupRoles;
    private ArrayList<GroupAttribute> groupAttributes;
	public Group() {
	    memberGroups = new TypedArrayList(Group.class,Group.class);
	    parentGroups = new TypedArrayList(Group.class,Group.class);
        groupRoles = new TypedArrayList(Role.class);
        groupAttributes = new TypedArrayList(GroupAttribute.class);
	}
	
	/**
     * @return the memberGroups
     */
    public ArrayList<Group> getMemberGroups() {
        return this.memberGroups;
    }

    /**
     * @param memberGroups the memberGroups to set
     */
    public void setMemberGroups(ArrayList<Group> memberGroups) {
        this.memberGroups = memberGroups;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	protected LinkedHashMap toStringMapper() {
            LinkedHashMap<String, Object> propMap = new LinkedHashMap<String, Object>();
            propMap.put("id", getId());
            propMap.put("name", getName());
            propMap.put("description", getDescription());
            return propMap;
	}

	public void refresh() {
		// not implemented unless needed
	}
    /**
     * @return the groupRoles
     */
    public ArrayList<Role> getGroupRoles() {
        return this.groupRoles;
    }
    /**
     * @param groupRoles the groupRoles to set
     */
    public void setGroupRoles(ArrayList<Role> groupRoles) {
        this.groupRoles = groupRoles;
    }

    /**
     * @return the parentGroups
     */
    public ArrayList<Group> getParentGroups() {
        return this.parentGroups;
    }

    /**
     * @param parentGroups the parentGroups to set
     */
    public void setParentGroups(ArrayList<Group> parentGroups) {
        this.parentGroups = parentGroups;
    }

    /**
     * @return the groupAttributes
     */
    public ArrayList<GroupAttribute> getGroupAttributes() {
        return this.groupAttributes;
    }

    /**
     * @param groupAttributes the groupAttributes to set
     */
    public void setGroupAttributes(ArrayList<GroupAttribute> groupAttributes) {
        this.groupAttributes = groupAttributes;
    }
}
