# REPORT

Cloud computing stands as a pivotal realm of investigation in contemporary Information Technology. Its significance burgeons due to the adept utilization of virtualization concepts, aiming to furnish IT services to cloud consumers on-demand, offering enhanced flexibility, availability, reliability, and scalability. E-governance, in recent times, has garnered substantial traction in numerous developing nations, with cloud computing playing a pivotal role in augmenting its efficiency and effectiveness. While cloud services allure a global audience with a plethora of offerings accessible via the internet, the caveat lies in their susceptibility to security threats. Notably, Distributed Denial of Service (DDoS) attacks loom large as a predominant security concern. These attacks incapacitate service provision by inundating network bandwidth and overwhelming service providers' computational resources. This paper delineates an experimental examination of the impact of DDoS attacks on Infrastructure as a Service (IaaS) configurations in CloudSim through simulation. By inducing flood attacks on IaaS-configured data centers within CloudSim, we assess key computational parameters such as memory consumption, storage utilization, and bandwidth consumption of virtual machines.

This project involves conducting an experimental analysis by simulating a DDOS attack on Infrastructure as a Service (IaaS) configurations in CloudSim. The goal is to evaluate the impact on computational parameters such as RAM consumption, storage utilization, and bandwidth consumption by virtual machines (VMs) during the attack compared to VMs operating under normal conditions.

This project works in 3 steps:

1. Configure the basic IaaS environment for cloud computing network
with 4 Virtual Machines (VM) in cloudsim for Analysis of DDOS in cloud
environment. 

2. Enable execution of instructions to close the previously running
simulation and enable request to the client for fresh request of simulation of
implementation of DDOS attack by enabling one of VM as attacker out of 4.

3. Graphical analysis of simulated result on RAM consumed, Storage
utilization and Bandwidth of each VM.

Finally, we have a graphical analysis of the comparison of RAM consumption, storage utilization, and bandwidth consumption between attack-scenario and normal scenario.

# USAGE

Set up cloudsim in your java ide and then add jfreechart library in your jar folder to run this
