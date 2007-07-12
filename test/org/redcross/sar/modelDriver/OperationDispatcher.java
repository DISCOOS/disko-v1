package org.redcross.sar.modelDriver;

import no.cmr.hrs.sar.tools.ElementDispatcher;
import no.cmr.hrs.sar.tools.ChangeObject;
import no.cmr.hrs.sar.tools.Changeable;
import no.cmr.hrs.sar.model.*;
import no.cmr.search.SearchResults;
import org.rescuenorway.saraccess.model.SarObject;
import org.rescuenorway.saraccess.model.SarBaseObject;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Christian Michelsen Research AS
 * User: Stian
 * Date: 10.jul.2007
 * Time: 14:12:39
 */
public class OperationDispatcher implements ElementDispatcher
{
    Operation oper;
    Map<String, SarBaseObject> objMap=new HashMap();

    public void addOperation(Operation
            aOperation)
            {
                oper=aOperation;
            }

            public void addObject(SarObject
            aObject, String addNodeToId)
            {
                oper.addObject(aObject);
                objMap.put(aObject.getID(),aObject);
            }

            public void addCheck(ChecklistNode
            aChecklistNode, String addNodeToID)
            {
                oper.getChecklist().add(aChecklistNode);
                objMap.put(aChecklistNode.getID(),aChecklistNode);
            }

            public void addMessage(Message
            aMessage, String addMessageToID)
            {
                oper.addMessage(aMessage);
                objMap.put(aMessage.getID(),aMessage);
            }

            public void merge(String fromOpId, String toOpId, long time, int status)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void change(ChangeObject aChangeObj)
            {
                SarBaseObject so=objMap.get(aChangeObj.getObjID());
                if (aChangeObj.getFactType() == Fact.FACTTYPE_RELATION) {
                    so.changeRelations(aChangeObj);
                } else {
                    ((Changeable)so).change(aChangeObj);
                }

            }

            public void addFact(Fact
            aFact, String addFactToID, long aTime)
            {
                oper.addFact(aFact);
            }

            public void addFactList(List
            aFactList, String addFactToID)
            {
                oper.addFactlist(aFactList);
                for (Iterator iterator = aFactList.iterator(); iterator.hasNext();)
                {
                    SarBaseObject fact = (SarBaseObject) iterator.next();
                    objMap.put(fact.getID(),fact);
                }
            }

            public void addChecklist(List
            aChecklist, String frontURL, String addFactToID)
            {
                Checklist lCheck = (Checklist) oper.getChecklist();
                lCheck.addChecklist(aChecklist);
                for (Iterator iterator = aChecklist.iterator(); iterator.hasNext();)
                {
                    SarBaseObject fact = (SarBaseObject) iterator.next();
                    objMap.put(fact.getID(),fact);
                }

            }

            public void addResource(Resource
            aResource, String addResourceToID, List
            factsAboutResource)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void newOperationType(List
            factList, List
            checkList, String frontURL, String operationId, String opType, long time)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void newOpTypes(List
            categories)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean delete(String aObjId, String aFieldName, int factType, String aComment, long aTime)
            {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean delete(String aObjId, String aFieldName, String aComment, long aTime)
            {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            //public abstract void newFactList(java.util.List aList);
            public String getBusName()
            {
                return "bus";
            }

            public void searchRes(SearchResults
            aResults, String type)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void newAdmList(List
            aList, String type)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

             public void addSearchPlanObject(EffortAllocation
            so)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }


    public Operation getOperation()
    {
        return oper;
    }

}
