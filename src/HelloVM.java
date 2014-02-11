/*================================================================================
Copyright (c) 2008 VMware, Inc. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, 
this list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice, 
this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.

* Neither the name of VMware, Inc. nor the names of its contributors may be used
to endorse or promote products derived from this software without specific prior 
written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL VMWARE, INC. OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.
================================================================================*/



import java.net.URL;

/**
 * Write a description of class HelloVM here.
 * 
 * @author Pawan Shrivastava
 * @version 4.3 Date Modified: 10/8/2013 Time: 2:14:59
 */


import java.io.IOException;
import com.vmware.vim25.ManagedEntityStatus;
import com.vmware.vim25.ResourceAllocationInfo;
import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.Network;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class HelloVM 
{
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		URL url = new URL("https://130.65.133.70/sdk");
		ServiceInstance si = new ServiceInstance(url, "administrator", "12!@qwQW", true);
		long end = System.currentTimeMillis();
		System.out.println("time taken:" + (end-start));
		Folder rootFolder = si.getRootFolder();
		String name = rootFolder.getName();
		System.out.println("root:" + name);
		ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
		if(mes==null || mes.length ==0)
		{
			return;
		}
		
		//VirtualMachine vm = (VirtualMachine) mes[3];		                                         
		System.out.println("\n============ Virtual Machines ============");
		ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(
				new String[][] { {"VirtualMachine", "name" }, }, true);
		VirtualMachine pawanvm = null;
		for(int i=0; i<vms.length; i++)
		{
			System.out.println("vm["+i+"]=" + vms[i].getName());
			if( vms[i].getName().equals("T19vm01Ubuntu32Pawan")){
				pawanvm = (VirtualMachine)vms[i];
			}
		}
				
		VirtualMachine vm = (VirtualMachine) pawanvm;
		VirtualMachineConfigInfo vmcinfo = vm.getConfig();
		VirtualMachineCapability vmc = vm.getCapability();
		final CodeRepository myvm = new CodeRepository("T19vm01Ubuntu32Pawan");
		Network[] vmNetwork = vm.getNetworks();		
		ManagedEntityStatus vmcpuid = vm.getConfigStatus();
		ResourceAllocationInfo cpuallocation = vmcinfo.getCpuAllocation();
		ResourceAllocationInfo memoryallocation = vmcinfo.getMemoryAllocation();
		ManagedEntityStatus vmHbStatus = vm.getGuestHeartbeatStatus();
		vm.getResourcePool();
		
		System.out.println("\n============ Statistics of my VM T19vm01Ubuntu32Pawan ============");
		System.out.println("Hello " + vm.getName());
		System.out.println("GuestOS: " + vmcinfo.getGuestFullName());
		System.out.println("Multiple snapshot supported: " + vmc.isMultipleSnapshotsSupported());
		System.out.println("VM HeartBeat Status: " + vmHbStatus);		
		System.out.println("VM Network Info: " + vmNetwork);
		System.out.println("Virtual " + vmcpuid);
		System.out.println("VM CPU allocation: " + cpuallocation);
		System.out.println("VM Memory allocation: " + memoryallocation);
		
		System.out.println("\n============ Starting the VM ping task ============");
		myvm.setAlarm();
		Thread t1 = new Thread() {
			public void run() {
				while (true) {
					try {
						myvm.setAlarm();
						boolean result = myvm.pingVM();
						if (result == true) {
					
							System.out.println("Pinging VM: VM is up and running...");
						} else {

							String state = myvm.getVMState();
							if (state.equalsIgnoreCase("poweredoff")) {
								System.out.println("VM is Powered off by user");
							} 
							else {
								System.out.println("State is Powered On & VM is not responding ...So its a failure");
								boolean hresult = myvm.pingSecondHost();
								if (hresult == true) {
									System.out.println("Alternate Host is working:- Start Cloning and Migrating ");
									try {
											myvm.cloneFromSnapshot("", "");    // clone from snapshot in same host
						

									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									System.out.println("Second Host is not working..Wait till it resumes..");
								}

							}

						}

					} catch (IOException e) {

						e.printStackTrace();
					}
					try {
						Thread.sleep(10000); // Check pinging after 10 Sec.
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
			}
		 	};
		 	
		 	Thread t2= new Thread(){
		 		public void run(){
		 			while(true)
		 			{
		 				myvm.snapShot("", "create");
		 				try {
							Thread.sleep(1200000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		 			}
		 		}
		 	};
		 	myvm.getAlarm();	//get Alarm and start monitoring
		 	t1.start();
		 	t2.start();
	    }
					
							;
						/*else {

							String state = myvm.getVMState();
							if (state.equalsIgnoreCase("poweredoff")) {
								System.out.println("VM is Powered off by user");
							} 
		/*
		if(vmHbStatus.toString().equals("green")){
			System.out.println("stat = green");
			Task task = vm.createSnapshot_Task("Pawansnap", "123", false, false);
			if(task.waitForTask()==Task.SUCCESS){
				System.out.println("Snapshot created successfully");
				
			}else
			{
				System.out.println("stat = red");
				System.out.println("Launching the VM clone task. " +
			    		"Please wait ...");
				myvm.cloneFromSnapshot("", "");
				VirtualMachineCloneSpec cloneSpec = 
				      new VirtualMachineCloneSpec();
			   
		    cloneSpec.setLocation(new VirtualMachineRelocateSpec());
		    cloneSpec.setPowerOn(false);
		    cloneSpec.setTemplate(false);

		    Task clone = vm.cloneVM_Task((Folder) vm.getParent(), 
		        "clone1", cloneSpec);
			}
			
					}
		
		
		si.getServerConnection().logout();
		
		
	
		*/
		
			
		
		
		
	}
	
	


