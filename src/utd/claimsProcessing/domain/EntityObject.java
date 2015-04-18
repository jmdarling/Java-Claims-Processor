package utd.claimsProcessing.domain;

import java.io.Serializable;

/**
 * This is the interface implemented by all Entity classes 
 * in this application. The ID assigned to each instance
 * must be unique among all instances of objects in the
 * same entity class. 
 */
public interface EntityObject extends Serializable
{
	String getID();
}
