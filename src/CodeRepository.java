

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.vmware.vim25.HostVMotionCompatibility;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineMovePriority;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSnapshotInfo;
import com.vmware.vim25.VirtualMachineSnapshotTree;
import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.AlarmManager;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.VirtualMachineSnapshot;


/**
 * Write a description of class Code Repository here.
 * 
 * @author Pawan Shrivastava
 * @version 4.3 Date Modified: 10/8/2013 Time: 2:14:59
 */

public class CodeRepository {
	// instance variables - replace the example below with your own
	private String vmname;
	private ServiceInstance si;
	private VirtualMachine vm;
	private static boolean valueSet=true;
	static String snapName;
	//private ResourcePool rp;
	
	/**
	 * Constructor for objects of class MyVM
	 */
	public CodeRepository() {
		// initialise instance variables
		try {
			
			this.si = new ServiceInstance(new URL(IpWarehouse.getVmwareHostURL()),
					IpWarehouse.getVmwareLogin(), IpWarehouse.getVmwarePassword(), true);
			
			//this.rp = (ResourcePool) new InventoryNavigator(rootFolder).searchManagedEntity("ResourcePool", SJSULAB.getRp1());
    		

		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}
	
	public CodeRepository(String vmname) {
		// initialise instance variables
		try {
			this.vmname = vmname;
			this.si = new ServiceInstance(new URL(IpWarehouse.getVmwareHostURL()),
					IpWarehouse.getVmwareLogin(), IpWarehouse.getVmwarePassword(), true);
			Folder rootFolder = si.getRootFolder();
			this.vm = (VirtualMachine) new InventoryNavigator(rootFolder)
					.searchManagedEntity("VirtualMachine", this.vmname);
			//this.rp = (ResourcePool) new InventoryNavigator(rootFolder).searchManagedEntity("ResourcePool", SJSULAB.getRp1());
			this.snapName="test1";

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		if (this.vm == null) {
			System.out.println("No VM " + vmname + " found");
			if (this.si != null){
				//this.si.getServerConnection().logout();
				
			}
		}
	}

	/**
	 * Destructor for objects of class MyVM
	 */
	protected void finalize() throws Throwable {
		//this.si.getServerConnection().logout(); // do finalization here
		super.finalize(); // not necessary if extending Object.
	}

	/**
	 * Power On the Virtual Machine
	 */
	public void powerOn() {
		try {
			System.out.println("Powered on");
			Task task = vm.powerOnVM_Task(null);
			if (task.waitForTask() == Task.SUCCESS) {
				System.out.println(vmname + " is powered on");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * Power Off the Virtual Machine
	 */
	public void powerOff() {
		try {
			System.out.println("Powered off");
			Task task = vm.powerOffVM_Task();
			if (task.waitForTask() == Task.SUCCESS) {
				System.out.println(vmname + "is powered off");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * Reset the Virtual Machine
	 */

	public void reset() {
		try {
			System.out.println("command: reset");
			Task task = vm.resetVM_Task();
			if (task.waitForTask() == Task.SUCCESS) {
				System.out.println(vmname + " reset");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * Suspend the Virtual Machine
	 */

	public void suspend() {
		try {
			System.out.println("command: suspend");
			Task task = vm.suspendVM_Task();
			if (task.waitForTask() == Task.SUCCESS) {
				System.out.println(vmname + " suspended");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * Put VM & Guest OS on Standby
	 */
	public void standBy() {
		try {
			System.out.println("command: stand by");
			vm.standbyGuest();
			System.out.println(vmname + " guest OS stoodby");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void helloVM() {
		try {
			long start = System.currentTimeMillis();

			long end = System.currentTimeMillis();
			System.out.println("time taken:" + (end - start));
			Folder rootFolder = si.getRootFolder();
			String name = rootFolder.getName();
			System.out.println("root:" + name);
			ManagedEntity[] mes;

			mes = new InventoryNavigator(rootFolder)
					.searchManagedEntities("VirtualMachine");

			if (mes == null || mes.length == 0) {
				return;
			}

			for (int i = 0; i < mes.length; i++) {
				VirtualMachine vm = (VirtualMachine) mes[i];

				VirtualMachineConfigInfo vminfo = vm.getConfig();
				VirtualMachineCapability vmc = vm.getCapability();
				
				
				System.out.println("Resource pool: " +vm.getResourcePool());
				System.out.println("Resource pool Owner: " +vm.getResourcePool().getOwner());
				System.out.println("Resource pool Parent: " +vm.getResourcePool().getParent());
				//System.out.println("Resource pool resource pools: " +vm.getResourcePool().getResourcePools().toString());
				System.out.println("Resource pool Values: " +vm.getResourcePool().getValues());
				System.out.println("Resource pool VM: " +vm.getResourcePool().getVMs().toString());
				//System.out.println("Resource pool Summary: " +vm.getResourcePool().getSummary().toString());
				System.out.println("");
				
				System.out.println("Hello " + vm.getName());
				System.out.println("VM Datastores: " +vm.getDatastores().toString());
			    System.out.println("VM Config: " +vm.getConfig().toString());
				System.out.println("VM Guest: " +vm.getGuest().getIpAddress());
				System.out.println("VM Parent: " +vm.getParent());
				System.out.println("VM resouce pool: " +vm.getResourcePool());
				System.out.println("VM Runtime: " +vm.getRuntime().toString());
				System.out.println("VM Storage: " +vm.getStorage().toString());
				System.out.println("VM Values: " +vm.getValues());
				
				System.out.println("");
				
				
				System.out.println("GuestOS: " + vminfo.getGuestFullName());
				System.out.println("GuestID: " + vminfo.getGuestId());
				System.out.println("GuestName: " + vminfo.getName());
				System.out.println("GuestDataStore URL: " + vminfo.getDatastoreUrl());
				System.out.println("");
				System.out.println("Multiple snapshot supported: "
						+ vmc.isMultipleSnapshotsSupported());
				
			}
		} catch (InvalidProperty e) {
			e.printStackTrace();
		} catch (RuntimeFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public Boolean pingHost() throws IOException
    {
		Folder rootFolder=si.getRootFolder();
		String hostIp;
		ManagedEntity[] hosts = new InventoryNavigator(rootFolder).searchManagedEntities(
				new String[][] { {"HostSystem", "name" }, }, true);
		
		hostIp=vm.getName();
		
		String newHostUrl="https://"+vm.getName()+"/sdk";
		//System.out.println(newHostUrl);
		
		ServiceInstance sitemp = new ServiceInstance(new URL(newHostUrl), IpWarehouse.getVmwareLogin(), IpWarehouse.getVmwarePassword(), true);
		Folder rf = sitemp.getRootFolder();
		ManagedEntity[] vms = new InventoryNavigator(rf).searchManagedEntities(
				new String[][] { {"VirtualMachine", "name" }, }, true);
		for(int i=0; i<vms.length; i++)
		{
			//System.out.println("vm["+i+"]=" + vms[i].getName());
			//System.out.println(vms[i].getParent());
			//System.out.println("");
			if(vms[i].getName().equalsIgnoreCase(vmname))
			{
				hostIp=hosts[0].getName();
				break;
			}
			else{
				hostIp=hosts[1].getName();
			}
			
		}
		
		//
		String pingResult = " ";
		Boolean reach=false;
		String pingCmd = "ping " + hostIp;

		
		Runtime r = Runtime.getRuntime();
		Process p = r.exec(pingCmd);

		BufferedReader in = new BufferedReader(new
		InputStreamReader(p.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
		//System.out.println(inputLine);
		pingResult += inputLine;
		}		
		in.close();
		if(pingResult.contains("Destination host unreachable"))
		{
		 System.out.println("Host Not Found/ Destination unreachable");
		 reach=false;
		}
		else
		{
			System.out.println("Host is live. Pinging on: "+hostIp);	
			reach=true;
		}
		
		return reach;
    }
	
	public Boolean pingSecondHost() throws IOException
    {
		Folder rootFolder=si.getRootFolder();
		String hostIp;
		ManagedEntity[] hosts = new InventoryNavigator(rootFolder).searchManagedEntities(
				new String[][] { {"HostSystem", "name" }, }, true);
		
		hostIp=hosts[0].getName();
		
		String newHostUrl="https://130.65.133.70/sdk";
		//System.out.println(newHostUrl);
		
		
		ServiceInstance sitemp = new ServiceInstance(new URL(newHostUrl), IpWarehouse.getVmwareLogin2(), IpWarehouse.getVmwarePassword2(), true);
		Folder rf = sitemp.getRootFolder();
		ManagedEntity[] vms = new InventoryNavigator(rf).searchManagedEntities(
				new String[][] { {"VirtualMachine", "name" }, }, true);
		for(int i=0; i<vms.length; i++)
		{
			//System.out.println("vm["+i+"]=" + vms[i].getName());
			//System.out.println(vms[i].getParent());
			//System.out.println("");
			if(vms[i].getName().equalsIgnoreCase(vmname))
			{
				hostIp=hosts[1].getName();
				break;
			}
			
		}
		
		//
		String pingResult = " ";
		Boolean reach=false;
		String pingCmd = "ping " + hostIp;

		
		Runtime r = Runtime.getRuntime();
		Process p = r.exec(pingCmd);

		BufferedReader in = new BufferedReader(new
		InputStreamReader(p.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
		//System.out.println(inputLine);
		pingResult += inputLine;
		}		
		in.close();
		if(pingResult.contains("Destination host unreachable"))
		{
			System.out.println("Second Host Not Found/ Destination unreachable");
			reach=false;
		}
		else
		{
			System.out.println("Second Host is live. Pinging on: "+hostIp);	
			reach=true;
		}
		
		return reach;
    }
	
	synchronized public Boolean pingVM() throws IOException
    {
		Folder rootFolder=si.getRootFolder();
		String hostIp;
		
		VirtualMachine vm = (VirtualMachine) new InventoryNavigator(
		        rootFolder).searchManagedEntity(
		            "VirtualMachine", vmname);
		
		hostIp=vm.getGuest().getIpAddress();
		System.out.println(hostIp);
		//
		String pingResult = " ";
		Boolean reach = false;
		String pingCmd = "ping " + hostIp;

		while (!valueSet) {
			try {
				wait();
				} catch (InterruptedException e) {
			
					e.printStackTrace();
				}

			}
		
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(pingCmd);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				// System.out.println(inputLine);
				pingResult += inputLine;
			}
			in.close();
		if (hostIp != null) {
			if (pingResult.contains("Destination host unreachable")) {
				System.out.println("Host Not Found/ Destination unreachable");
				reach = false;
				valueSet = false;
			} else {
				System.out.println("Host is live. Pinging on: " + hostIp);
				reach = true;
				notifyAll();
			}
		}
		else{
			System.out.println("Host IP is Null:"+hostIp);
			reach=false;
			valueSet=false;
		}
		return reach;
    }
	
	public String getVMState()
	{
		
		VirtualMachineRuntimeInfo vmri=vm.getRuntime();
		String state=vmri.getPowerState().toString();
		return state;
	}
   
	//we are not using this method so no need for sync.
	 synchronized public void  cloneToSameHost(String vmname1, String clonename1) throws Exception
	  {
	    if(vmname1.equalsIgnoreCase(null) || clonename1.equalsIgnoreCase(null))
	    {
	      System.out.println("Usage: java CloneVM <url> " +
	      "<username> <password> <vmname> <clonename>");
	      System.exit(0);
	    }

	    String vmname = vmname1;
	    String cloneName = clonename1;

	    Folder rootFolder = si.getRootFolder();
	    VirtualMachine vm = (VirtualMachine) new InventoryNavigator(
	        rootFolder).searchManagedEntity(
	            "VirtualMachine", vmname);

	    if(vm==null)
	    {
	      System.out.println("No VM " + vmname + " found");
	      //si.getServerConnection().logout();
	      return;
	    }

	    VirtualMachineCloneSpec cloneSpec = 
	      new VirtualMachineCloneSpec();
	    cloneSpec.setLocation(new VirtualMachineRelocateSpec());
	    cloneSpec.setPowerOn(false);
	    cloneSpec.setTemplate(false);
	    

	    Task task = vm.cloneVM_Task((Folder) vm.getParent(), 
	        cloneName, cloneSpec);
	    System.out.println("Launching the VM clone task. " +
	    		"Please wait ...");

	    String status = task.waitForTask();
	    if(status==Task.SUCCESS)
	    {
	      System.out.println("VM got cloned successfully.");
	      notifyAll();
	    }
	    else
	    {
	      System.out.println("Failure -: VM cannot be cloned");
	    }
	  }
	 
	public void checkHost() {
		Folder rootFolder = si.getRootFolder();
		try {
		System.out.println("============ Data Centers ============");
		ManagedEntity[] dcs;
		
			dcs = new InventoryNavigator(rootFolder).searchManagedEntities(
					new String[][] { {"Datacenter", "name" }, }, true);
		
		for(int i=0; i<dcs.length; i++)
		{
			System.out.println("Datacenter["+i+"]=" + dcs[i].getName());
		}
		
		System.out.println("\n============ Virtual Machines ============");
		ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(
				new String[][] { {"VirtualMachine", "name" }, }, true);
		for(int i=0; i<vms.length; i++)
		{
			System.out.println("vm["+i+"]=" + vms[i].getName());
			
		}

		System.out.println("\n============ Hosts ============");
		ManagedEntity[] hosts = new InventoryNavigator(rootFolder).searchManagedEntities(
				new String[][] { {"HostSystem", "name" }, }, true);
		
		for(int i=0; i<hosts.length; i++)
		{
			System.out.println("host["+i+"]=" + hosts[i].getName());
		}
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	synchronized public void snapShot(String vmname1, String op1) {
		if (vmname1.equals(null) || op1.equals(null)) {
			System.out.println("Usage: java VMSnapshot <url> "
					+ "<username> <password> <vmname> <op>");
			System.out.println("op - list, create, remove, "
					+ "removeall, revert");
			System.exit(0);
		}
		String vmname=this.vmname;
		//String vmname = vmname1;
		String op = op1;
		// please change the following three depending your op
		String snapshotname = "snaptest";
		String desc = "A description for sample snapshot";
		boolean removechild = true;

		Folder rootFolder = si.getRootFolder();
		VirtualMachine vm;
		try {
			while(!valueSet)
			{
				wait();
			}
			vm = (VirtualMachine) new InventoryNavigator(rootFolder)
					.searchManagedEntity("VirtualMachine", vmname);
			if (vm == null) {
				System.out.println("No VM " + vmname + " found");
				//si.getServerConnection().logout();
				return;
			}

			if ("create".equalsIgnoreCase(op)) {
				if(snapName.equalsIgnoreCase("test2"))
				{
					snapName="test1";
				}
				else if(snapName.equalsIgnoreCase("test1"))
				{
					snapName="test2";
				}
				Task task = vm.createSnapshot_Task(snapName, desc, false,false);
				
				if (task.waitForTask() == Task.SUCCESS) {
					System.out.println("Snapshot was created.");
					try {
						if(snapName.equalsIgnoreCase("test1"))
						{
							deletePreviousSnap(vm,"test2");	
						}
						else{
							deletePreviousSnap(vm,"test1");
						}
						//delete previous snapshot
						//cloneFromSnapshot(vmname, vmname+"_fromSS");	//code for snapshot to vm 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					valueSet=true;
					notify();
				}
			} else if ("list".equalsIgnoreCase(op)) {
				listSnapshots(vm);
			
				
			} else if (op.equalsIgnoreCase("revert")) {
				VirtualMachineSnapshot vmsnap = getSnapshotInTree(vm,
						snapshotname);
				if (vmsnap != null) {
					Task task = vmsnap.revertToSnapshot_Task(null);
					if (task.waitForTask() == Task.SUCCESS) {
						System.out.println("Reverted to snapshot:"
								+ snapshotname);	
					}
				//Code to migrate	
					//migrateToAnotherHost(vmname, "");
					
				}
			} else if (op.equalsIgnoreCase("removeall")) {
				Task task = vm.removeAllSnapshots_Task();
				if (task.waitForTask() == Task.SUCCESS) {
					System.out.println("Removed all snapshots");
				}
			} else if (op.equalsIgnoreCase("remove")) {
				VirtualMachineSnapshot vmsnap = getSnapshotInTree(vm,
						snapshotname);
				if (vmsnap != null) {
					Task task = vmsnap.removeSnapshot_Task(removechild);
					if (task.waitForTask() == Task.SUCCESS) {
						System.out.println("Removed snapshot:" + snapshotname);
					}
				}
			}else if (op.equalsIgnoreCase("deleteP")) {
				deletePreviousSnap(vm,snapshotname);
			} 
			else {
				System.out.println("Invalid operation");
				return;
			}
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//snapshots methods
	static void deletePreviousSnap(VirtualMachine vm,String snapName)
	{
		if (vm == null ) 
	    {
	      //
			System.out.println("In delete PreSnap: Cannot delete snapshot because no VM found..");
	    }

	    VirtualMachineSnapshotTree[] snapTree = 
	        vm.getSnapshot().getRootSnapshotList();
	    if(snapTree!=null)
	    {
	      ManagedObjectReference mor = findSnapshotInTree(
		          snapTree, snapName);
		      
		      if(mor!=null)
		      {
			       VirtualMachineSnapshot vsnap=new VirtualMachineSnapshot(vm.getServerConnection(), mor);
			       try {
					vsnap.removeSnapshot_Task(false);
				} catch (TaskInProgress e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RuntimeFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      }
	    }
	    else{
	    	 System.out.println("SnapTree is null So cannot delete Snapshot..");
	    }
	}
	static VirtualMachineSnapshot getSnapshotInTree(VirtualMachine vm, String snapName)
		  {
		    if (vm == null || snapName == null) 
		    {
		      return null;
		    }

		    VirtualMachineSnapshotTree[] snapTree = 
		        vm.getSnapshot().getRootSnapshotList();
		    if(snapTree!=null)
		    {
		      ManagedObjectReference mor = findSnapshotInTree(
		          snapTree, snapName);
		      
		      if(mor!=null)
		      {
		        return new VirtualMachineSnapshot(
		            vm.getServerConnection(), mor);
		      }
		    }
		    return null;
		  }

		  static ManagedObjectReference findSnapshotInTree(
		      VirtualMachineSnapshotTree[] snapTree, String snapName)
		  {
		    for(int i=0; i <snapTree.length; i++) 
		    {
		      VirtualMachineSnapshotTree node = snapTree[i];
		      if(snapName.equals(node.getName()))
		      {
		        return node.getSnapshot();
		        
		      } 
		      else 
		      {
		        VirtualMachineSnapshotTree[] childTree = 
		            node.getChildSnapshotList();
		        if(childTree!=null)
		        {
		          ManagedObjectReference mor = findSnapshotInTree(
		              childTree, snapName);
		          if(mor!=null)
		          {
		            return mor;
		          }
		        }
		      }
		    }
		    return null;
		  }

		  static void listSnapshots(VirtualMachine vm)
		  {
		    if(vm==null)
		    {
		      return;
		    }
		    VirtualMachineSnapshotInfo snapInfo = vm.getSnapshot();
		    VirtualMachineSnapshotTree[] snapTree = 
		      snapInfo.getRootSnapshotList();
		    printSnapshots(snapTree);
		  }

		  static void printSnapshots(
		      VirtualMachineSnapshotTree[] snapTree)
		  {
		    for (int i = 0; snapTree!=null && i < snapTree.length; i++) 
		    {
		      VirtualMachineSnapshotTree node = snapTree[i];
		      System.out.println("Snapshot Name : " + node.getName());           
		      VirtualMachineSnapshotTree[] childTree = 
		        node.getChildSnapshotList();
		      if(childTree!=null)
		      {
		        printSnapshots(childTree);
		      }
		    }
		  }
		  
		  //// snap shot methods finish
		  
		 synchronized public boolean cloneFromSnapshot(String vmname1, String clonename1) throws Exception
		  {
			 valueSet=false;		//this will stop ping and also create snapshots
			 vmname1=this.vmname;
					 
		    // String cloneName = clonename1;
			String cloneName= vmname+"_SS";
		    Folder rootFolder = si.getRootFolder();
		    VirtualMachine vm = (VirtualMachine) new InventoryNavigator(
		        rootFolder).searchManagedEntity(
		            "VirtualMachine", vmname1);

		    if(vm==null)
		    {
		      System.out.println("No VM " + vmname1 + " found");
		      //si.getServerConnection().logout();
		      return false;
		    }

		    VirtualMachineCloneSpec cloneSpec = 
		      new VirtualMachineCloneSpec();
		    cloneSpec.setLocation(new VirtualMachineRelocateSpec());
		    cloneSpec.setPowerOn(false);
		    cloneSpec.setTemplate(false);
		    //ManagedObjectReference snapshot=cloneSpec.getSnapshot();
			//cloneSpec.setSnapshot(cloneSpec.getSnapshot());
			cloneSpec.snapshot=vm.getCurrentSnapShot().getMOR();

		    Task task = vm.cloneVM_Task((Folder) vm.getParent(), 
		        cloneName, cloneSpec);
		    System.out.println("Launching the VM clone task from SNAPSHOT.... " +
		    		"Please wait ...");

		    String status = task.waitForTask();
		    if(status==Task.SUCCESS)
		    {
		      System.out.println("VM got cloned successfully FROM SNAPSHOT....");
		      boolean migrateResult=migrateToAnotherHost(cloneName, "");
		      if(migrateResult)
		      {
		    	 notify();
		     	 return true;
		      }
		    }
		    else
		    {
		      System.out.println("Failure -: VM cannot be cloned FROM SNAPSHOT...");
		      
		    }
		    return false;
		  }
		  
	//method migrate to another host
	 public boolean migrateToAnotherHost(String vmname1 , String newHostName1) {
		if (vmname1.equals(null) || newHostName1.equals(null)) {
			System.out.println("Usage: java MigrateVM <url> "
					+ "<username> <password> <vmname> <newhost>");
			System.exit(0);
		}
		
		valueSet=false;
		System.out.println("The value is false so it will not ping untill it migrate and starts...");
		
		String vmname=vmname1;
		//String vmname = vmname1;
		//String newHostName = newHostName1;
		String newHostName = "";
		try {
			Folder rootFolder = si.getRootFolder();
		
			VirtualMachine vm = (VirtualMachine) new InventoryNavigator(
					rootFolder).searchManagedEntity("VirtualMachine", vmname);
			
			
			//Start: To get another host IP
			
			ManagedEntity[] hosts = new InventoryNavigator(rootFolder).searchManagedEntities(
					new String[][] { {"HostSystem", "name" }, }, true);
			for(int i=0; i<hosts.length; i++)
			{
				System.out.println("host["+i+"]=" + hosts[i].getName());
			}
			
			newHostName=hosts[0].getName();
			String newHostUrl= "https://130.65.133.70/sdk";
			
			ServiceInstance sitemp = new ServiceInstance(new URL(newHostUrl), IpWarehouse.getVmwareLogin(), IpWarehouse.getVmwarePassword(), true);
			Folder rf = sitemp.getRootFolder();
			ManagedEntity[] vms = new InventoryNavigator(rf).searchManagedEntities(
					new String[][] { {"VirtualMachine", "name" }, }, true);
			for(int i=0; i<vms.length; i++)
			{
				//System.out.println("vm["+i+"]=" + vms[i].getName());
				//System.out.println(vms[i].getParent());
				//System.out.println("");
				if(vms[i].getName().equalsIgnoreCase(vmname))
				{
					newHostName=hosts[0].getName();
					break;
				}
				
			}
			
			//End
			
			if(newHostName.equals(null)||newHostName.equalsIgnoreCase(""))
			{
				System.out.println("New Host is invalid OR Null");
				System.exit(0);
			}
			HostSystem newHost = (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity("HostSystem",newHostName);
			ComputeResource cr = (ComputeResource) newHost.getParent();
			
			String[] checks = new String[] { "cpu", "software" };
			HostVMotionCompatibility[] vmcs = si.queryVMotionCompatibility(vm,
					new HostSystem[] { newHost }, checks);

			String[] comps = vmcs[0].getCompatibility();
			if (checks.length != comps.length) {
				System.out.println("CPU/software NOT compatible. Exit.");
				//si.getServerConnection().logout();
				return false;
			}

			Task task = vm.migrateVM_Task(cr.getResourcePool(),newHost,
					VirtualMachineMovePriority.highPriority,
					VirtualMachinePowerState.poweredOff);
			

			if (task.waitForTask() == Task.SUCCESS) {
				System.out.println("VMotioned Migrated..!");
				
				//Before rename delete previous one
				VirtualMachine oldvm = (VirtualMachine) new InventoryNavigator(rootFolder)
				.searchManagedEntity("VirtualMachine", this.vmname);
				Task offoldvm=oldvm.powerOffVM_Task();
				if(offoldvm.waitForTask()==Task.SUCCESS)
				{
				Task removeOld = oldvm.destroy_Task();
				if(removeOld.waitForTask()==Task.SUCCESS)
				{
					System.out.println("Old Vm is deleted successfully...");
				}

				Task task1 = vm.rename_Task(this.vmname);
				if (task1.waitForTask() == Task.SUCCESS) {

					Task task2 = vm.powerOnVM_Task(newHost);
					if (task2.waitForTask() == Task.SUCCESS) {
						System.out.println("VM On! ");
						valueSet = true;
						return true;
						
					} else {
						System.out.println("Power On failed!");
					}
				} else {
					System.out.println("Not renamed..So no power on...");
				}
			} else {
				System.out.println("VMotion failed!");
				TaskInfo info = task.getTaskInfo();
				System.out.println(info.getError().getFault());
				
			}
			}
			else{
				System.out.println("Old Vm is not poweroff..So no Delete/ not renamed new one..");
			}
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void setAlarm()
	{
		try {
			//vmname=this.vmname;
			CreateVirtualmachineAlarm.createAlarm(vmname);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void getAlarm()
	{
		//vmname=this.vmname;
		InventoryNavigator inv = new InventoryNavigator(
		        si.getRootFolder());
		    VirtualMachine vm;
			try {
				vm = (VirtualMachine)inv.searchManagedEntity(
				        "VirtualMachine", vmname);
		   
		    if(vm==null)
		    {
		      System.out.println("Cannot find the VM " + vmname + "\nExisting...");
		     // si.getServerConnection().logout();
		      return;
		    }
		    
		    AlarmManager alarmMgr = si.getAlarmManager();
		    Alarm alarms[]=alarmMgr.getAlarm(vm);
		   
		    for(int i=0;i<alarms.length;i++)
		    {
		    	System.out.println("Alarm Name "+i+" "+alarms[i].getAlarmInfo().getName());
		    	
		    	//System.out.println(alarms[i].getAlarmInfo().getExpression());
		    }
		  
			} catch (InvalidProperty e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RuntimeFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}