package org.redcross.sar.wp.logistikk;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.AssignmentImpl;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IAssignmentListIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.util.mso.Selector;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.EnumSet;
import java.util.Vector;

/**
 *  Model used for the Assignment Display part of the Logistics Panel
 */
public class AssignmentDisplayModel implements IMsoUpdateListenerIf, ComponentListener
{
    private AssignmentScrollPanel m_selectableAssignments;
    private AssignmentScrollPanel m_priAssignments;
    private IMsoEventManagerIf m_eventManager;
    private IAssignmentListIf m_AssignmentList;

    /**
     * Types of selection in left panel
     */
    public enum Selection
    {
        READY, ALLOCATED, ASSIGNED, EXECUTING, FINISHED
    }

    /**
     * Initial value for selection
     */
    private Selection m_assigmentSelection = Selection.READY;

    /**
     * Selectors for left panel
     */
    private Selector<IAssignmentIf> m_selectableSelector;


    /**
     * Status for left panel
     */
    private IAssignmentIf.AssignmentStatus m_selectableStatus;

    /**
     * Selectors for right panel
     */
    private Selector<IAssignmentIf> m_priSelector;

    /**
     * Popup handler for selection
     */
    SelectionPopupHandler m_selectionMenu;

    /**
     * Value for determinining in which panel a ready assignment shall be placed (divider between right and left part)
     */
    private int m_readyDivider;

    public AssignmentDisplayModel(AssignmentScrollPanel panel1, AssignmentScrollPanel panel2, IMsoEventManagerIf anEventManager, IAssignmentListIf anAssignmentList)
    {
        m_selectableAssignments = panel1;
        m_selectableAssignments.setMaxIndex(-1);
        m_selectableSelector = IAssignmentIf.READY_SELECTOR;
        m_selectableAssignments.setHeaderPopupHandler(new SelectionPopupHandler(this));

        m_priAssignments = panel2;
        m_priAssignments.setMinIndex(0);
        m_priSelector = IAssignmentIf.READY_SELECTOR;
        m_priAssignments.getHeaderLabel().setText("Pri");
        m_priAssignments.setSelectedStatus(IAssignmentIf.AssignmentStatus.READY);

        m_eventManager = anEventManager;
        m_eventManager.addClientUpdateListener(this);

        m_AssignmentList = anAssignmentList;

        panel2.getParent().addComponentListener(this);

        setSelection(Selection.FINISHED);
        handleMsoUpdateEvent(null);
    }

    /**
     * Handle change of data in the MSO model event
     * @param e The event
     */
    public void handleMsoUpdateEvent(MsoEvent.Update e)
    {
        java.util.List<IAssignmentIf> priList = selectAssignments(m_priSelector);
        m_priAssignments.setAssignmentList(priList);

        setSelectableList();

        calculateDivider();
        renderData();
    }

    private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT);

    /**
     * Decide if  this object is interesting for update events
     * @param aMsoObject The actual object
     * @return <code><code/> if interesting, <code>false<code/> otherwise
     */
    public boolean hasInterestIn(IMsoObjectIf aMsoObject)
    {
        return myInterests.contains(aMsoObject.getMsoClassCode());
    }

    /**
     * Set the list that is source for the left panel
     */
    private void setSelectableList()
    {
        java.util.List<IAssignmentIf> selectionList = m_assigmentSelection == Selection.READY ? m_priAssignments.getAssignmentList() : selectAssignments(m_selectableSelector);
        m_selectableAssignments.setAssignmentList(selectionList);
        m_selectableAssignments.setSelectedStatus(m_selectableStatus);
    }

    /**
     * Generate al list of assignments based on a selector, sorted by priority and assignment number.
     * @param aSelector The Selector
     * @return The list of assignments
     */
    private java.util.List<IAssignmentIf> selectAssignments(Selector<IAssignmentIf> aSelector)
    {
        return m_AssignmentList.selectItems(aSelector, IAssignmentIf.PRIORITY_AND_NUMBER_COMPARATOR);
    }

    public void componentResized(ComponentEvent e)
    {
        calculateDivider();
        renderData();
    }

    public void componentMoved(ComponentEvent e)
    {
    }

    public void componentShown(ComponentEvent e)
    {
    }

    public void componentHidden(ComponentEvent e)
    {
    }

    /**
     * Draw the two panels.
     */
    private void renderData()
    {
        renderPriPanel();
        renderSelectablePanel();
    }

    /**
     * Calculate value of divider, depending on height of panel.
     */
    private void calculateDivider()
    {
        m_readyDivider = m_priAssignments.getMaxNonscrollItems();
    }

    /**
     * Set selection for left panel.
     * @param aSelection Value to set
     */
    public void setSelection(Selection aSelection)
    {
        if (m_assigmentSelection != aSelection)
        {
            m_assigmentSelection = aSelection;
            m_selectableAssignments.getHeaderLabel().setText(m_assigmentSelection.name());
            selectionChanged();
        }
    }

    /**
     * Get selection for left panel
     * @return Selection value.
     */
    public Selection getSelection()
    {
        return m_assigmentSelection;
    }

    /**
     * Handle change of selection.
     * Sets selector and redraws left panel.
     */
    private void selectionChanged()
    {
        switch (m_assigmentSelection)
        {
            case READY:
                m_selectableSelector = IAssignmentIf.READY_SELECTOR;
                m_selectableStatus = IAssignmentIf.AssignmentStatus.READY;
                break;
            case ALLOCATED:
                m_selectableSelector = IAssignmentIf.ALLOCATED_SELECTOR;
                m_selectableStatus = IAssignmentIf.AssignmentStatus.ALLOCATED;
                break;
            case ASSIGNED:
                m_selectableSelector = IAssignmentIf.ASSIGNED_SELECTOR;
                m_selectableStatus = IAssignmentIf.AssignmentStatus.ASSIGNED;
                break;
            case EXECUTING:
                m_selectableSelector = AssignmentImpl.EXECUTING_SELECTOR;
                m_selectableStatus = IAssignmentIf.AssignmentStatus.EXECUTING;
                break;
            case FINISHED:
                m_selectableSelector = AssignmentImpl.FINISHED_SELECTOR;
                m_selectableStatus = IAssignmentIf.AssignmentStatus.FINISHED;
                break;
        }
        setSelectableList();
        renderSelectablePanel();
    }

    /**
     * Draw right panel.
     */
    private void renderPriPanel()
    {
        m_priAssignments.setMaxIndex(m_readyDivider - 1);
        m_priAssignments.renderPanel();
    }

    /**
     * Draw left panel.
     */
    private void renderSelectablePanel()
    {
        if (m_assigmentSelection == Selection.READY)
        {
            m_selectableAssignments.setMinIndex(m_readyDivider);
        } else
        {
            m_selectableAssignments.setMinIndex(0);
        }
        System.out.println("List size:" + m_selectableAssignments.getAssignmentList().size());
        m_selectableAssignments.renderPanel();
    }

    /**
     * RadioButton used in the popup menu
     */
    public static class SelectButton extends JRadioButtonMenuItem
    {
        final Selection m_selection;

        public SelectButton(AbstractAction anAction, Selection aSelection)
        {
            super(anAction);
            m_selection = aSelection;
        }
    }

    /**
     * Handler of popup events, after the are detected by a {@link org.redcross.sar.wp.logistikk.PopupListener}
     */
    public static class SelectionPopupHandler extends AbstractPopupHandler
    {
        private final JPopupMenu m_menu = new JPopupMenu();
        private final AssignmentDisplayModel m_model;
        private final Vector<SelectButton> m_buttons = new Vector<SelectButton>(5);
        private final ButtonGroup m_buttonGroup = new ButtonGroup();

        public SelectionPopupHandler(AssignmentDisplayModel aModel)
        {
            super();
            m_model = aModel;

            addButton(buttonWithAction("Klar", Selection.READY));
            addButton(buttonWithAction("Neste", Selection.ALLOCATED));
            addButton(buttonWithAction("Tildelt", Selection.ASSIGNED));
            addButton(buttonWithAction("Startet", Selection.EXECUTING));
            addButton(buttonWithAction("Ferdig", Selection.FINISHED));
        }

        /**
         * Add a radio button to the structures
         * @param aButton The button to add
         */
        private void addButton(SelectButton aButton)
        {
            m_menu.add(aButton);
            m_buttons.add(aButton);
            m_buttonGroup.add(aButton);
        }

        private SelectButton buttonWithAction(String aText, final Selection aSelection)
        {
            AbstractAction action = new AbstractAction(aText)
            {
                public void actionPerformed(ActionEvent e)
                {
                    m_model.setSelection(aSelection);
                }
            };
            return new SelectButton(action, aSelection);
        }

        /**
         * Show the popup menu.
         * @param e Event that was triggered.
         */
        public JPopupMenu getMenu(MouseEvent e)
        {
            Selection s = m_model.getSelection();
            for (SelectButton b : m_buttons)
            {
                if (b.m_selection == s)
                {
                    b.setSelected(true);
                    break;
                }
            }
            return m_menu;
        }
    }
}
