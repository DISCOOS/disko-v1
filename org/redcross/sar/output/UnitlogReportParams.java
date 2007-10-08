package org.redcross.sar.output;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IPersonnelIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitListIf;


public class UnitlogReportParams {
	public final static String UNIT_TYPE_NUMBER	= "UnitTypeAndNumber";
	
	public final static String LEADER_NAME	= "LeaderName";
	public final static String LEADER_TELEPHONE	= "LeaderTelephone";
	public final static String CO_LEADER_NAME = "CoLeaderName";
	public final static String CO_LEADER_TELEPHONE = "CoLeaderTelephone";
	
	public final static String PERSONELL_LIST = "PersonellList";//not in use. Made for sending an array to printReport
	public final static String KEY_PERSON_NAME_PREFIX = "PersonName";
	public final static String KEY_PERSON_TELE_PREFIX = "PersonTele";
	public int maxPersonsInPrint = 10;
	
	public final static String CALLSIGNS = "Callsigns";//not in use
	public final static String KEY_CALLSIGN_PREFIX = "Callsign";
	public final static String KEY_UNIT_PREFIX = "Unit";
	public int maxCallSignsInPrint = 8;
	
	public final static String ASSIGNMENTS_LIST = "AssignmentsList";//not in use. Made for sending an array to printReport
	public final static String KEY_ASSIGNMENT_PREFIX = "Assignment";
	public final static String KEY_ASSIGNMENT_STATUS_PREFIX = "AssignmentStatus";
	public int maxAssignmentsInPrint = 8;
	
	public final static String PERSONELL_RELEASED_LIST = "PersonellReleasedList";//not in use. Made for sending an array to printReport
	public final static String KEY_RELEASED_PREFIX = "Released";
	public int maxReleasedInPrint = 8;
	
	public final static String REMARKS = "Remarks";
	public final static String EQUIPMENT = "Equipment";
	//Utstyr??
	
	private Map<String, Object> unitParams = new HashMap<String, Object>() ;
	private String[][] personell = null;
	private String[][] callSigns = null;
	private String[][] assignments = null;
	private String[] released = null;
		
	public UnitlogReportParams(IUnitListIf unitList, int unitNr){		
		extractParams(unitList, unitNr);
	}

	private void extractParams(IUnitListIf unitList, int unitNr){
		
		if(unitList != null){		
			IUnitIf unit = unitList.getUnit(unitNr);
			if(unit != null){
				unitParams.put(UNIT_TYPE_NUMBER, unit.getTypeAndNumber());
				IPersonnelIf leader = unit.getUnitLeader();
				unitParams.put(LEADER_NAME, leader.getFirstname() + " " + leader.getLastname());
				unitParams.put(LEADER_TELEPHONE, leader.getTelephone1());
				//co-leader mangler
				//todo
				
				makePersonelList(unit);
				
				makeCallSignsList(unitList, unit);
				
				makeAssignmentList(unit);		
				
				//remarks
				unitParams.put(REMARKS, unit.getRemarks());
				
				//equipment
				//todo		
			}		
			else 
				System.out.println("Unit er tom");
		}
		else 
			System.out.println("Unitliste er tom.");
	}
	
	public Map getUnitlogReportParams(){
		return unitParams;
	}

	private void makePersonelList(IUnitIf unit){
		//Personell liste
		Collection <IPersonnelIf> personelList = unit.getUnitPersonnelItems();
		int personsCount = personelList.size();
		String personInfo = new String();		
		Iterator personIter = personelList.iterator();
		int personCount = 0;
		String keyPersonName = new String();
		String keyPersonTele = new String();
		String keyReleased = new String();
		while (personIter.hasNext()) {			
			IPersonnelIf person = (IPersonnelIf)personIter.next();
			personInfo = person.getFirstname() + " " + person.getLastname();
			personell[personCount][0] = personInfo;
			personell[personCount][1] = person.getTelephone1();
			keyPersonName = KEY_PERSON_NAME_PREFIX + Integer.toString(personCount+1);
			keyPersonTele = KEY_PERSON_TELE_PREFIX + Integer.toString(personCount+1);
			unitParams.put(keyPersonName, personInfo);
			unitParams.put(keyPersonTele, person.getTelephone1());
			if(person.getStatus() == person.getStatus().RELEASED){				
				if (released == null){
					released[0] = person.getFirstname()+" "+person.getLastname();
					keyReleased = KEY_RELEASED_PREFIX + Integer.toString(released.length+1);
					unitParams.put(keyReleased, released[0]);
				}
				else{
					released[released.length] = person.getFirstname()+" "+person.getLastname();					
					keyReleased = KEY_RELEASED_PREFIX + Integer.toString(released.length+1);
					unitParams.put(keyReleased , released[released.length]);
				}
			}			
		}
		for (int j = 0; j < (maxPersonsInPrint-personsCount); j++){
			keyPersonName = KEY_PERSON_NAME_PREFIX + Integer.toString((personsCount+1+j));
			keyPersonTele = KEY_PERSON_TELE_PREFIX + Integer.toString((personsCount+1+j));
			unitParams.put(keyPersonName, "");
			unitParams.put(keyPersonTele, "");
		}
		int releasedCount = released.length;
		for (int j = 0; j < (maxReleasedInPrint-releasedCount); j++){
			keyReleased = KEY_RELEASED_PREFIX + Integer.toString((releasedCount+1+j));
			unitParams.put(keyReleased, "");
		}
		personCount++;
	}
	
	private void makeCallSignsList(IUnitListIf unitList, IUnitIf unit){
		//callsigns
		callSigns[0][1] = unit.getCallSign();
		callSigns[0][0] = unit.getUnitNumber();		
		int unitCount = unitList.size();
		Collection <IUnitIf> unitCollection = unitList.getItems();
		Iterator unitIter = unitCollection.iterator();
		int iter2 = 1;
		String keyCallSign = KEY_CALLSIGN_PREFIX + Integer.toString(iter2);
		String keyUnit = KEY_UNIT_PREFIX + Integer.toString(iter2);
		unitParams.put(keyCallSign, unit.getCallSign());
		unitParams.put(keyUnit, unit.getUnitNumber());
		while (unitIter.hasNext()){
			IUnitIf unit2 = (IUnitIf)unitIter.next();
			if (!callSigns[0][0].equalsIgnoreCase(unit2.getUnitNumber())){
				callSigns[iter2][0] = unit2.getUnitNumber();
				callSigns[iter2][1] = unit2.getCallSign();
				keyCallSign = KEY_CALLSIGN_PREFIX + Integer.toString(iter2);
				keyUnit = KEY_UNIT_PREFIX + Integer.toString(iter2);
				unitParams.put(keyCallSign, unit.getCallSign());
				unitParams.put(keyUnit, unit.getUnitNumber());
			}
			iter2++;
		}
		for (int j = 0; j < (maxCallSignsInPrint-unitCount); j++){
			keyCallSign = KEY_PERSON_NAME_PREFIX + Integer.toString((unitCount+1+j));
			keyUnit = KEY_PERSON_TELE_PREFIX + Integer.toString((unitCount+1+j));
			unitParams.put(keyCallSign, "");
			unitParams.put(keyUnit, "");
		}
	}
	
	private void makeAssignmentList(IUnitIf unit){
		//assignments
		//IAssignmentListIf assignments = unit.getUnitAssignments();
		Collection<IAssignmentIf> assignmentsList = unit.getUnitAssignmentsItems();
		int assignmentCount = assignmentsList.size();
		Iterator assignmentIter = assignmentsList.iterator();
		String keyAssignmentNr = new String();
		String keyAssignmentStatus = new String();
		int iter3 = 1;
		while (assignmentIter.hasNext()){
			IAssignmentIf assignment = (IAssignmentIf)assignmentIter.next();
			assignments[iter3][0] = assignment.getTypeAndNumber();
			assignments[iter3][1] = assignment.getStatusText();
			keyAssignmentNr = "KEY_ASSIGNMENT_PREFIX" + Integer.toString(iter3);
			keyAssignmentStatus = "KEY_ASSIGNMENT_STATUS_PREFIX" + Integer.toString(iter3);
			unitParams.put(keyAssignmentNr, assignment.getTypeAndNumber());
			unitParams.put(keyAssignmentStatus, assignment.getStatusText());
			iter3++;
		}
		for (int j = 0; j < (maxAssignmentsInPrint-assignmentCount); j++){
			keyAssignmentNr = KEY_PERSON_NAME_PREFIX + Integer.toString((assignmentCount+1+j));
			keyAssignmentStatus = KEY_PERSON_TELE_PREFIX + Integer.toString((assignmentCount+1+j));
			unitParams.put(keyAssignmentNr, "");
			unitParams.put(keyAssignmentStatus, "");
		}
	}
	
	
}


