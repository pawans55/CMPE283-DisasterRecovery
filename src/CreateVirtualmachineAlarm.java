

import java.net.URL;

import com.vmware.vim25.Action;
import com.vmware.vim25.AlarmAction;
import com.vmware.vim25.AlarmSetting;
import com.vmware.vim25.AlarmSpec;
import com.vmware.vim25.AlarmTriggeringAction;
import com.vmware.vim25.MethodAction;
import com.vmware.vim25.MethodActionArgument;
import com.vmware.vim25.SendEmailAction;
import com.vmware.vim25.StateAlarmExpression;
import com.vmware.vim25.StateAlarmOperator;
import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.AlarmManager;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * http://vijava.sf.net
 * @author Pawan Shrivastava
 */

public class CreateVirtualmachineAlarm 
{
	static Boolean bool=false;
  public static void createAlarm(String vmname1) throws Exception 
  {
	  URL url = new URL("https://130.65.133.70/sdk");
	  ServiceInstance si = new ServiceInstance(url, "administrator", "12!@qwQW", true);
	 /* ServiceInstance si = new ServiceInstance(
		        new URL(IpWarehouse.getVmwareHostURL()), IpWarehouse.getVmwareLogin(),IpWarehouse.getVmwarePassword(), true);*/

    String vmname = vmname1;
    InventoryNavigator inv = new InventoryNavigator(si.getRootFolder());
    VirtualMachine vm = (VirtualMachine)inv.searchManagedEntity(
            "VirtualMachine", vmname);
   
    if(vm==null)
    {
      System.out.println("Cannot find the VirtualMachine " + vmname 
        + "\nExisting...");
      //si.getServerConnection().logout();
      return;
    }
    
    AlarmManager alarmMgr = si.getAlarmManager();
    
    //This will remove alarm if it exists...
    Alarm alarms[]=alarmMgr.getAlarm(vm);
    for(int i=0;i<alarms.length;i++)
    {
    	if(alarms[i].getAlarmInfo().getName().equalsIgnoreCase("VmPowerStateAlarmqwert"))
    	{
    		alarms[i].removeAlarm();
    	}
    }
    
    AlarmSpec spec = new AlarmSpec(); 
    StateAlarmExpression expression = 
      createStateAlarmExpression();
    AlarmAction emailAction = createAlarmTriggerAction(createEmailAction());
    spec.setAction(emailAction);
    spec.setExpression(expression);
    spec.setName("VmPowerStateAlarmqwert");
    spec.setDescription("Monitor VM state and send email " + "and power it on if VM powers off");
    spec.setEnabled(true);    
    
    AlarmSetting as = new AlarmSetting();
    as.setReportingFrequency(0); //as often as possible
    as.setToleranceRange(0);
    
    spec.setSetting(as);
    System.out.println(vm.getName());
    alarmMgr.createAlarm(vm, spec);
    si.getServerConnection().logout();
  }

  static StateAlarmExpression createStateAlarmExpression()
  {
    StateAlarmExpression expression = 
      new StateAlarmExpression();
    expression.setType("VirtualMachine");
    expression.setStatePath("runtime.powerState");
    expression.setOperator(StateAlarmOperator.isEqual);
    expression.setRed("poweredOff");
    
    
    return expression;
  }

  static MethodAction createPowerOnAction() 
  {
    MethodAction action = new MethodAction();
    action.setName("PowerOffVM_Task"); //PowerOffVM_Task
    
    MethodActionArgument argument = new MethodActionArgument();
    argument.setValue(null);
    action.setArgument(new MethodActionArgument[] { argument });
   
    return action;
  }
  
  static SendEmailAction createEmailAction() 
  {
    SendEmailAction action = new SendEmailAction();
    action.setToList("pawanshrivastav55@gmail.com");
    action.setCcList("pawanshrivastav55@gmail.com");
    action.setSubject("Alarm - {alarmName} on {targetName}\n");
    action.setBody("Description:{eventDescription}\n"
        + "TriggeringSummary:{triggeringSummary}\n"
        + "newStatus:{newStatus}\n"
        + "oldStatus:{oldStatus}\n"
        + "target:{target}");
    
    return action;
  }
  
 

  static AlarmTriggeringAction createAlarmTriggerAction(Action action) 
  {
    AlarmTriggeringAction alarmAction = 
      new AlarmTriggeringAction();
    alarmAction.setYellow2red(true);
    
    alarmAction.setAction(action);
    return alarmAction;
  }
}