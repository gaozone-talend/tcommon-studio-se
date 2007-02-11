/*
 * Created on Jul 17, 2004
 */
package com.quantum.editors.graphical.model.commands;

import org.eclipse.gef.commands.Command;

import com.quantum.editors.graphical.model.Relationship;
import com.quantum.editors.graphical.model.Table;

/**
 * Command to delete relationship
 * 
 * @author Phil Zoio
 */
public class DeleteRelationshipCommand extends Command
{

	private Table foreignKeySource;
	private Table primaryKeyTarget;
	private Relationship relationship;

	public DeleteRelationshipCommand(Table foreignKeySource, Table primaryKeyTarget, Relationship relationship)
	{
		super();
		this.foreignKeySource = foreignKeySource;
		this.primaryKeyTarget = primaryKeyTarget;
		this.relationship = relationship;
	}

	/**
	 * @see Removes the relationship
	 */
	public void execute()
	{
		foreignKeySource.removeForeignKeyRelationship(relationship);
		primaryKeyTarget.removePrimaryKeyRelationship(relationship);
		relationship.setForeignKeyTable(null);
		relationship.setPrimaryKeyTable(null);
	}

	/**
	 * @see Restores the relationship
	 */
	public void undo()
	{
		relationship.setForeignKeyTable(foreignKeySource);
		relationship.setForeignKeyTable(primaryKeyTarget);
		foreignKeySource.addForeignKeyRelationship(relationship);
		primaryKeyTarget.addPrimaryKeyRelationship(relationship);
	}

}

