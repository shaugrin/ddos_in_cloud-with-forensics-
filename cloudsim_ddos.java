package org.cloudbus.cloudsim.examples;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.JFrame;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class CloudSimExample1 {
    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmlist;
    private static int vm1RamBeforeAttack;
    private static long vm1BwBeforeAttack;
    private static int vm1RamAfterAttack;
    private static long vm1BwAfterAttack;


    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Log.printLine("Starting CloudSimExample1...");

        try {
            int num_user = 1;
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;
            CloudSim.init(num_user, calendar, trace_flag);

            // Create Datacenter
            Datacenter datacenter0 = createDatacenter("Datacenter_0");

            // Create DatacenterBroker
            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            vmlist = new ArrayList<Vm>();

            int vmid = 0;
            int mips = 1000;
            long size = 10000;
            int ram = 512;
            long bw = 1000;
            int pesNumber = 1;
            String vmm = "Xen";

            Vm vm1 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
            vmid++;
            Vm vm2 = new Vm(vmid, brokerId, mips * 2, pesNumber, ram - 256, bw, size * 2, vmm, new CloudletSchedulerTimeShared());
            vmid++;
            Vm vm3 = new Vm(vmid, brokerId, mips / 2, pesNumber, ram + 256, bw, size * 3, vmm, new CloudletSchedulerTimeShared());
            vmid++;
            Vm vm4 = new Vm(vmid, brokerId, mips * 4, pesNumber, ram, bw, size * 4, vmm, new CloudletSchedulerTimeShared());
            vmid++;

            vmlist.add(vm1);
            vmlist.add(vm2);
            vmlist.add(vm3);
            vmlist.add(vm4);

            broker.submitVmList(vmlist);

            cloudletList = new ArrayList<Cloudlet>();

            int id = 0;
            long length = 400000;
            long fileSize = 300;
            long outputSize = 300;
            UtilizationModel utilizationModel = new UtilizationModelFull();

            Cloudlet cloudlet1 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            cloudlet1.setUserId(brokerId);
            id++;
            Cloudlet cloudlet2 = new Cloudlet(id, length * 2, pesNumber, fileSize * 2, outputSize / 3, utilizationModel, utilizationModel, utilizationModel);
            cloudlet2.setUserId(brokerId);
            id++;
            Cloudlet cloudlet3 = new Cloudlet(id, length / 2, pesNumber, fileSize * 3, outputSize * 3, utilizationModel, utilizationModel, utilizationModel);
            cloudlet3.setUserId(brokerId);
            Cloudlet cloudlet4 = new Cloudlet(id, length / 3, pesNumber, fileSize / 3, outputSize / 2, utilizationModel, utilizationModel, utilizationModel);
            cloudlet4.setUserId(brokerId);
            Cloudlet cloudlet5 = new Cloudlet(id, length * 3, pesNumber, fileSize / 2, outputSize / 4, utilizationModel, utilizationModel, utilizationModel);
            cloudlet5.setUserId(brokerId);
            Cloudlet cloudlet6 = new Cloudlet(id, length / 4, pesNumber, fileSize * 4, outputSize * 4, utilizationModel, utilizationModel, utilizationModel);
            cloudlet6.setUserId(brokerId);
            Cloudlet cloudlet7 = new Cloudlet(id, length * 4, pesNumber, fileSize, outputSize * 2, utilizationModel, utilizationModel, utilizationModel);
            cloudlet7.setUserId(brokerId);
            Cloudlet cloudlet8 = new Cloudlet(id, length, pesNumber, fileSize / 4, outputSize / 3, utilizationModel, utilizationModel, utilizationModel);
            cloudlet8.setUserId(brokerId);

            cloudletList.add(cloudlet1);
            cloudletList.add(cloudlet2);
            cloudletList.add(cloudlet3);
            cloudletList.add(cloudlet4);
            cloudletList.add(cloudlet5);
            cloudletList.add(cloudlet6);
            cloudletList.add(cloudlet7);
            cloudletList.add(cloudlet8);

            broker.submitCloudletList(cloudletList);

            // Selecting VM2 as the attacker (you can modify this based on your requirements)
            Vm attackerVM = vm2;

            // Create a Cloudlet representing the DDoS attack
            Cloudlet ddosAttack = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            ddosAttack.setUserId(brokerId);

            // Create a list to hold the DDoS attack cloudlets
            List<Cloudlet> ddosAttackList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {  // Sending 100 repetitive requests for the attack
                ddosAttackList.add(ddosAttack);
            }

         // Submit the DDoS attack cloudlet list to the broker
            broker.submitCloudletList(ddosAttackList);

         // Collect resource consumption data before the attack
            List<Double> ramConsumptionDataBeforeAttack = new ArrayList<>();
            for (Vm vm : vmlist) {
                int ramAllocated = vm.getRam(); // Assuming getRam() returns an int
                ramConsumptionDataBeforeAttack.add((double) ramAllocated);
            }

            // ... (rest of the code)

            CloudSim.startSimulation();

            // Collect resource consumption data after the attack
            List<Double> ramConsumptionDataAfterAttack = new ArrayList<>();
            for (Vm vm : vmlist) {
                int ramAllocated = vm.getRam(); // Assuming getRam() returns an int
                ramConsumptionDataAfterAttack.add((double) ramAllocated);
            }

            CloudSim.stopSimulation();

            // Calculate the impact on VM1's resources
            int vm1RamBeforeAttack = ramConsumptionDataBeforeAttack.get(0).intValue(); // Assuming VM1 is the first in the list
            long vm1BwBeforeAttack = vm1.getBw(); // Assuming getBw() returns a long

            int vm1RamAfterAttack = ramConsumptionDataAfterAttack.get(0).intValue(); // Assuming VM1 is the first in the list
            long vm1BwAfterAttack = vm1.getBw(); // Assuming getBw() returns a long

            int ramImpact = vm1RamAfterAttack - vm1RamBeforeAttack;
            long bwImpact = vm1BwAfterAttack - vm1BwBeforeAttack;

            // Print the results
            System.out.println("Impact on VM1's Resources:");
            System.out.println("RAM Usage Before: " + vm1RamBeforeAttack);
            System.out.println("RAM Usage After: " + vm1RamAfterAttack);
            System.out.println("Bandwidth Usage Before: " + vm1BwBeforeAttack);
            System.out.println("Bandwidth Usage After: " + vm1BwAfterAttack);
            System.out.println("Impact on RAM: " + ramImpact);
            System.out.println("Impact on Bandwidth: " + bwImpact);

            
            createChart(vm1RamBeforeAttack, vm1BwBeforeAttack, vm1RamAfterAttack, vm1BwAfterAttack);

            

            List<Cloudlet> newList = broker.getCloudletReceivedList();
            printCloudletList(newList);

            Log.printLine("CloudSimExample1 finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unwanted errors happen");
        }
    }

    private static Datacenter createDatacenter(String name) {
        List<Host> hostList = new ArrayList<Host>();

        List<Pe> peList = new ArrayList<Pe>();
        int mips = 1000;

        peList.add(new Pe(0, new PeProvisionerSimple(mips)));

        int hostId = 0;
        int ram = 2048;
        long storage = 1000000;
        int bw = 10000;

        hostList.add(new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList,
                new VmSchedulerTimeShared(peList)));

        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double time_zone = 10.0;
        double cost = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.001;
        double costPerBw = 0.0;
        LinkedList<Storage> storageList = new LinkedList<Storage>();

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost,
                costPerMem, costPerStorage, costPerBw);

        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    private static DatacenterBroker createBroker() {
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = " ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent
                + "Time" + indent + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent
                        + cloudlet.getVmId() + indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent
                        + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent
                        + dft.format(cloudlet.getFinishTime()));
            }
        }    
    }
    
    private static void createChart(int ramBefore, long bwBefore, int ramAfter, long bwAfter) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(ramBefore, "RAM Before Attack", "VM1");
        dataset.addValue(bwBefore, "Bandwidth Before Attack", "VM1");
        dataset.addValue(ramAfter, "RAM After Attack", "VM1");
        dataset.addValue(bwAfter, "Bandwidth After Attack", "VM1");

        JFreeChart barChart = ChartFactory.createBarChart(
                "DDOS Attack Impact on VM1 Resources",
                "Resource",
                "Usage",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        JFrame frame = new JFrame("Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(barChart));
        frame.pack();
        frame.setVisible(true);
    }
}
