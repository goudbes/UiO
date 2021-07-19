/**
*Added reading from file for the patients and appended patients to the tabell
*/

import java.io.*;
import java.util.*;

public class Oblig7 {

  //ANSI escape codes for formatting output
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";

  public static void main(String[] args) {
    Tabell<Patient> patients = new Tabell<Patient>(0);
    Tabell<Drug> drugs = new Tabell<Drug>(0);
    SortertEnkelListe doctors = new SortertEnkelListe();
    EnkelReseptListe prescriptions = new EnkelReseptListe();

    readFile("dataset.txt",prescriptions,patients,drugs,doctors);
    do {

      System.out.println(ANSI_CYAN + "\n");
      System.out.println("--------------------------------------------------------------");
      System.out.println("                Doctors and prescriptions                     ");
      System.out.println("--------------------------------------------------------------");
      System.out.println("\n1 - Add new drug.");
      System.out.println("2 - Add new doctor.");
      System.out.println("3 - Add new patient.");
      System.out.println("4 - Add new prescription.");
      System.out.println("5 - View the list of drugs.");
      System.out.println("6 - View the list of doctors.");
      System.out.println("7 - View the list of patients.");
      System.out.println("8 - View the list of prescriptions.");
      System.out.println("9 - Get prescribed drug.");
      System.out.println("--------------------------------------------------------------");
      System.out.println("                        Statistics                            ");
      System.out.println("--------------------------------------------------------------");
      System.out.println("10 - Addictive drugs.");
      System.out.println("11 - Blue prescriptions.");
      System.out.println("12 - Doctor's prescriptions.");
      System.out.println("13 - Opioids");
      System.out.println("14 - Exit" + ANSI_RESET);
      System.out.print("\nSelect a Menu Option: ");
      try {
        Scanner input = new Scanner(System.in);
        int in = input.nextInt();// Get user input from the keyboard
        int i=0;

        switch (in) {

          case 1:  addDrug(drugs);
          break;
          case 2:  addDoctor(doctors);
          break;
          case 3:  addPatient(patients);
          break;
          case 4:  addPrescription(prescriptions,patients,doctors,drugs);
          break;
          case 5:  printDrugs(drugs,1);
          break;
          case 6:  printDoctors(doctors,2);
          break;
          case 7:  printPatients(patients,4);
          break;
          case 8:  printPrescriptions(prescriptions,3);
          break;
          case 9:  usePrescription(prescriptions,patients);
          break;
          case 10:  addictiveInfo(prescriptions,patients);
          break;
          case 11:  getBlue(patients);
          break;
          case 12:  doctorsInfo(doctors);
          break;
          case 13:  opioidsInfo(doctors,patients);
          break;
          case 14:  quit(prescriptions, patients, drugs, doctors);
          break;

        }
      } catch (Exception e) {
        System.out.println(ANSI_RED  + "Program failed due to: " + e +" " + ANSI_RESET);
      }
    }
    while(true);
  }


  public static void menues(int i) {
    if (i == 1) {
      System.out.println("--------------------------------------------------------------");
      System.out.println("                         Drugs                                ");
      System.out.println("--------------------------------------------------------------");
      System.out.println("id, name, form, type, price, number/volume, active substance [, strength]");
    }
    if (i == 2) {
      System.out.println("--------------------------------------------------------------");
      System.out.println("                         Doctors                              ");
      System.out.println("--------------------------------------------------------------");
      System.out.println("Name, Agreement number / 0 if none");
    }
    if (i == 3) {
      System.out.println("--------------------------------------------------------------");
      System.out.println("                         Prescriptions                        ");
      System.out.println("--------------------------------------------------------------");
      System.out.println("nr, While/Blue, Unique patient number, Doctor's name, Drug id, Reit)");
    }
    if (i == 4) {
      System.out.println("--------------------------------------------------------------");
      System.out.println("                         Patients                             ");
      System.out.println("--------------------------------------------------------------");
      System.out.println("nr, Name, National identity number, Address, Postal code)");
    }
  }

  public static void readFile(String filename, EnkelReseptListe prescriptions,
  Tabell<Patient> patients, Tabell<Drug> drugs, SortertEnkelListe doctors) {

    try{
      File file = new File(filename);

      if(!file.exists()) {
        System.out.println("This file doesn't exist.");
      }
      Scanner input = new Scanner(file);

      while (input.hasNextLine()) {
        String line = input.nextLine();
        // System.out.println(line);
        if (line.contains("Personer")) {
          line = input.nextLine();
          int i = 0;
          while (!line.isEmpty()) {
            addPatients(i++,patients,line.split(",[ ]*"));
            line = input.nextLine();
          }
        } else if (line.contains("Legemidler")) {
          line = input.nextLine();
          int i = 0;
          while(!line.isEmpty()) {
            addDrugs(i++,drugs,line.split(",[ ]*"));
            line = input.nextLine();
          }
        } else if (line.contains("Leger")) {
          line = input.nextLine();
          int i = 0;
          while(!line.isEmpty()) {
            addDoctors(i++,doctors,line.split(",[ ]*"));
            line = input.nextLine();
          }
        } else if (line.contains("Resepter")) {
          line = input.nextLine();
          int i = 0;
          while(!line.isEmpty()) {
            addPrescription(i++,prescriptions,line.split(",[ ]*"), patients, drugs,doctors);
            line = input.nextLine();
          }
        } else if (line.isEmpty()) {
          continue;
        }
      }

    } catch (Exception e) {
      System.out.println(ANSI_RED + " Error: " + e + " " + ANSI_RESET);
    }
  }

  /**
  *Adds a new prescription to the list with prescriptions, plus for each
  *patient and doctor
  *@param i unique number
  *@param prescriptions List with prescriptions
  *@param line Array that holds all the data for Prescription
  *@param patients List with patients
  *@param drugs List with drugs
  *@param doctors List with doctors
  */
  public static void addPrescription(int i, EnkelReseptListe prescriptions,
  String[] line, Tabell<Patient> patients, Tabell<Drug> drugs, SortertEnkelListe doctors) {
    Prescription p = new Prescription(i,drugs.find(Integer.parseInt(line[4])),line[3],
    Integer.parseInt(line[2]),Integer.parseInt(line[5]),
    line[1]);
    prescriptions.add(p);
    patients.get(p.getPatientsNum()).getListe().add(p);
    Doctor d = (Doctor) doctors.find(p.getDoctorsName());
    d.getListe().add(p);
    // if (prescriptions.find(i).getIdentificationNumber()==i) {
    //   System.out.println("Prescription was successfully added.");
    // } else {
    //   System.out.println("Prescription couldn't be added.");
    // }
  }

  /**
  *Adding patient to the list with patients
  *@param i identification numberOfPills
  *@param patients List with patients
  *@param line Array with data for patient
  */
  public static void addPatients(int i, Tabell<Patient> patients, String[] line) {
    patients.add(new Patient(Integer.parseInt(line[0]),line[1],line[2],line[3],line[4]),i);
    // if (patients.get(i).getName().equals(line[1])) {
    //   System.out.println("Patient was successfully added.");
    // } else {
    //   System.out.println("Patient couldn't be added.");
    // }
  }

  /**
  *Adding a doctor to the list with doctors
  *@param i Unique number
  *@param doctors List with doctors
  *@param line Array with data for the doctor
  */
  public static void addDoctors(int i, SortertEnkelListe  doctors, String[] line) {
    if (line[1].equals("0")) {
      doctors.add(new Doctor(line[0]));
    } else {
      doctors.add(new Generalpractitian(line[0],Integer.parseInt(line[1])));
    }
    // if (doctors.find(line[0]).toString().equals(line[0])) {
    //   System.out.println("Doctor was successfully added.");
    // } else {
    //   System.out.println("Doctor couldn't be added.");
    // }
  }

  /**
  *Adding a new drug to the list with drugs
  *@param i Unique number
  *@param drugs List with drugs
  *@param line Array with data for the drug
  */
  public static void addDrugs(int i, Tabell<Drug> drugs, String[] line) {
    if (line[3].equalsIgnoreCase("a")) {
      if (line[2].equalsIgnoreCase("mikstur")) {
        drugs.add(new OpioidMixture(line[1],Integer.parseInt(line[0]),Double.parseDouble(line[4]),
        Integer.parseInt(line[7]),Double.parseDouble(line[5]),Double.parseDouble(line[6])),i);
      } else if (line[2].equalsIgnoreCase("pille")) {
        drugs.add(new OpioidPill(line[1],Integer.parseInt(line[0]),Double.parseDouble(line[4]),
        Integer.parseInt(line[7]),Integer.parseInt(line[5]),Double.parseDouble(line[6])),i);
      }

    } else if (line[3].equalsIgnoreCase("b")) {
      if (line[2].equalsIgnoreCase("mikstur")) {
        drugs.add(new AddictiveMixture(line[1],Integer.parseInt(line[0]),Double.parseDouble(line[4]),
        Integer.parseInt(line[7]),Double.parseDouble(line[5]),Double.parseDouble(line[6])),i);
      } else if (line[2].equalsIgnoreCase("pille")) {
        drugs.add(new AddictivePill(line[1],Integer.parseInt(line[0]),Double.parseDouble(line[4]),
        Integer.parseInt(line[7]),Integer.parseInt(line[5]),Double.parseDouble(line[6])),i);
      }
    } else if (line[3].equalsIgnoreCase("c")) {
      if (line[2].equalsIgnoreCase("mikstur")) {
        drugs.add(new OrdinaryMixture(line[1],Integer.parseInt(line[0]),Double.parseDouble(line[4]),
        Double.parseDouble(line[5]),Double.parseDouble(line[6])),i);
      } else if (line[2].equalsIgnoreCase("pille")) {
        drugs.add(new OrdinaryPill(line[1],Integer.parseInt(line[0]),Double.parseDouble(line[4]),
        Integer.parseInt(line[5]),Double.parseDouble(line[6])),i);
      }
    }
    // if (drugs.find(i).getName().equals(line[1])) {
    //   System.out.println("The drug was successfully added.");
    // } else {
    //   System.out.println("The drug couldn't be added.");
    // }
  }

  /**
  *Printing all the drugs to the console
  *@param drugs List with drugs
  */
  public static void printDrugs(Tabell<Drug> drugs,int i) {
    menues(i);
    if (drugs.getLength()==0) {
      System.out.println(ANSI_RED + "There is no data to view." + ANSI_RESET);
    }
    for (Drug d: drugs) {
      d.info();
    }
  }

  /**
  *Printing all the doctors to the console
  *@param doctors List with doctors
  */
  public static void printDoctors(SortertEnkelListe doctors,int i) {
    menues(i);
    if(doctors.tom()) {
      System.out.println(ANSI_RED + "There is no data to view."+ ANSI_RESET);
    }
    Iterator<Doctor> doc = doctors.iterator();
    while (doc.hasNext()) {
      Doctor doctor = doc.next();
      doctor.info();
    }
  }

  /**
  *Quit program
  */
  public static void quit(EnkelReseptListe prescriptions,
  Tabell<Patient> patients, Tabell<Drug> drugs, SortertEnkelListe doctors) {
    writeToFile(prescriptions, patients, drugs, doctors);
    System.exit(0);
  }

  /**
  *Printing all the prescriptions to the console
  *@param prescriptions List with all the prescriptions
  */
  public static void printPrescriptions(EnkelReseptListe prescriptions,int i) {
    menues(i);
    if (prescriptions.empty()) {
      System.out.println(ANSI_RED + "There is no data to view." + ANSI_RESET);
    }
    Iterator<Prescription> presc = prescriptions.iterator();
    while (presc.hasNext()) {
      presc.next().info();
    }
  }

  /**
  *Printing all the patients to the console
  *@param patients List with patients
  */
  public static void printPatients(Tabell<Patient> patients,int i) {
    menues(i);
    if (patients.getLength()==0) {
      System.out.println(ANSI_RED + "There is no data to view" + ANSI_RESET);
    }
    for (Patient p: patients) {
      System.out.println(p.getIdentificationNumber() + ", " + p.getName() +
      ", " + p.getNationalIdentityNumber() + ", " + p.getAddress() +
      ", " + p.getIndex());
    }
  }

  /**
  *Adding a new patient to the list of patients
  *@param patients List with patients
  */
  public static void addPatient(Tabell<Patient> patients) {
    System.out.println("Adding a new patient");
    System.out.println("");
    Scanner input = new Scanner(System.in);
    System.out.print("Patient's name: ");
    String name = input.nextLine();
    System.out.print("National identity number (11 digits): ");
    String perIdNum = input.nextLine();
    if (perIdNum.length()!=11) {
      throw new IllegalArgumentException(ANSI_RED + "Invalid National Identity number." + ANSI_RESET);
    }
    System.out.print("Adress: ");
    String address = input.nextLine();
    System.out.print("Postal code(4 digits): ");
    String index = input.nextLine();
    if (index.length()!=4) {
      throw new IllegalArgumentException(ANSI_RED + "Invalid postal code." + ANSI_RESET);
    }
    int position = patients.getLength();
    if (patients.add(new Patient(position,name,perIdNum,address,index),position)) {
      System.out.println(ANSI_GREEN + "Patient " + name + " was successfully added."+ANSI_RESET);
    } else {
      System.out.println(ANSI_RED + "Patient " + name + " couldn't be added."+ ANSI_RESET);
    }
  }

  /**
  *Adding a new doctor to the list with doctors
  *@param doctors List with doctors
  */
  public static void addDoctor(SortertEnkelListe doctors) {
    System.out.println("Adding a new doctor");
    System.out.println("");
    Scanner in = new Scanner(System.in);
    System.out.print("Doctors name: ");
    String name = in.nextLine();
    System.out.println("Agreement number (0 if doctor has none): ");
    int  an = in.nextInt();
    if (an==0) {
      doctors.add(new Doctor(name));
      Doctor doctor = (Doctor) doctors.find(name);
      if (doctor.getName().equals(name)) {
        System.out.println(ANSI_GREEN + "Doctor " + name + " was successfully added."+ ANSI_RESET);
      } else {
        System.out.println(ANSI_RED + "Doctor " + name + " was not added." + ANSI_RESET);
      }
    } else if (an>0){
      doctors.add(new Generalpractitian(name, an));
      Generalpractitian doctor = (Generalpractitian) doctors.find(name);
      if (doctor.getName().equals(name)) {
        System.out.println(ANSI_GREEN + "General practician " + name + " was successfully added."+ ANSI_RESET);
      } else {
        System.out.println(ANSI_RED + "General practician " + name + " was not added." + ANSI_RESET);
      }
    }
    else {
      throw new DrugsException(ANSI_RED + "Doctor couldn't be added: wrong agreement number" + ANSI_RESET);
    }
  }

  /**
  *Adding a new drug to the list with drugs
  *@param drugs List with drugs
  */
  public static void addDrug(Tabell<Drug> drugs) {
    System.out.println("Adding a new drug");
    int position = drugs.getLength();
    Scanner in = new Scanner(System.in);
    System.out.print("Type (mikstur/pille): ");
    String type = in.nextLine();
    if (!type.equals("mikstur") && !type.equals("pille")) {
      throw new IllegalArgumentException(ANSI_RED + "Wrong type."+ ANSI_RESET);
    }
    System.out.print("Form (a,b,c): ");
    String form = in.nextLine();
    if (!form.equals("a") && !form.equals("b") && !form.equals("c")) {
      throw new IllegalArgumentException(ANSI_RED + "Wrong form." + ANSI_RESET);
    }
    System.out.print("Name: ");
    String name = in.nextLine();
    System.out.print("Price: ");
    Double price = Double.parseDouble(in.nextLine());
    System.out.print("Active substance: ");
    Double activeSubstance = Double.parseDouble(in.nextLine());
    if (form.equals("a")) {
      System.out.print("Strength: ");
      int strength = Integer.parseInt(in.nextLine());
      if (type.equals("mikstur")) {
        System.out.print("Volume: ");
        Double volume = Double.parseDouble(in.nextLine());
        drugs.add(new OpioidMixture(name,position,price,strength,volume,
        activeSubstance),position);
      } else if (type.equals("pille")) {
        System.out.print("Number of pills: ");
        int numberOfPills = Integer.parseInt(in.nextLine());
        drugs.add(new OpioidPill(name,position,price,strength,numberOfPills,
        activeSubstance),position);
      }
    } else if(form.equals("b")) {
      System.out.print("Addictive: ");
      int addictive = Integer.parseInt(in.nextLine());
      if (type.equals("mikstur")) {
        System.out.print("Volume: ");
        Double volume = Double.parseDouble(in.nextLine());
        drugs.add(new AddictiveMixture(name,position,price,addictive,volume,
        activeSubstance),position);
      } else if (type.equals("pille")) {
        System.out.print("Number of pills: ");
        int numberOfPills = Integer.parseInt(in.nextLine());
        drugs.add(new AddictivePill(name,position,price,addictive,numberOfPills,
        activeSubstance),position);
      }
    } else {
      if (type.equals("mikstur")) {
        System.out.print("Volume: ");
        Double volume = Double.parseDouble(in.nextLine());
        drugs.add(new OrdinaryMixture(name,position,price,volume,
        activeSubstance),position);
      } else if (type.equals("pille")) {
        System.out.print("Number of pills: ");
        int numberOfPills = Integer.parseInt(in.nextLine());
        drugs.add(new OrdinaryPill(name,position,price,numberOfPills,
        activeSubstance),position);
      }
    }
    if (drugs.find(position).getName().equals(name)) {
      System.out.println(ANSI_GREEN + "Drug was successfully added." + ANSI_RESET);
    } else {
      System.out.println(ANSI_RED + "Drug couldn't be added." + ANSI_RESET);
    }
  }

  /**
  *Adding a new prescriptions
  *@param prescriptions List with prescriptions
  *@param patients List with patients
  *@param doctors List with doctors
  *@param drugs List with drugs
  */
  public static void addPrescription(EnkelReseptListe prescriptions,
  Tabell<Patient> patients, SortertEnkelListe doctors, Tabell<Drug> drugs ) {
    System.out.print("Add new prescription");
    Scanner in = new Scanner(System.in);
    int position = prescriptions.getLast().getIdentificationNumber()+1;
    System.out.println("Type (blaa/hvit): ");
    String type = in.nextLine();
    if (!type.equals("blaa") && !type.equals("hvit")) {
      throw new IllegalArgumentException(ANSI_RED + "Wrong type."+ ANSI_RESET);
    }
    System.out.print("Patient's unique number: ");
    int idnum = Integer.parseInt(in.nextLine());
    if (patients.get(idnum)==null) {
      throw new DrugsException(ANSI_RED + "Couldn't be found." + ANSI_RESET);
    }
    System.out.print("Doctor's name: ");
    String doctor = in.nextLine();
    if (doctors.find(doctor)==null) {
      throw new DrugsException(ANSI_RED + "Doctor couldn't be found."+ ANSI_RESET);
    }
    System.out.print("Drug's unique number: ");
    int drugId = Integer.parseInt(in.nextLine());
    if (drugs.get(drugId)==null) {
      throw new DrugsException(ANSI_RED + "Drug couldn't be found." + ANSI_RESET);
    }
    System.out.print("Reit: ");
    int reit = Integer.parseInt(in.nextLine());
    if (reit < 0) {
      throw new IllegalArgumentException(ANSI_RED + "Invalid reit." + ANSI_RESET);
    }

    Prescription p = new Prescription(position,drugs.get(drugId),doctor,
    idnum,reit,type);
    prescriptions.add(p);

    patients.get(p.getPatientsNum()).getListe().add(p);
    Doctor d = (Doctor) doctors.find(p.getDoctorsName());
    d.getListe().add(p);

    if (prescriptions.find(position).getIdentificationNumber()==position) {
      System.out.println(ANSI_GREEN + "Prescription was successfully added." + ANSI_RESET);
    } else {
      System.out.println(ANSI_RED + "Prescription couldn't be added." +ANSI_RESET);
    }

    if (patients.get(p.getPatientsNum()).getListe().find(p.getIdentificationNumber()).
    getIdentificationNumber()==position) {
      System.out.println(ANSI_GREEN + "Prescription was successfully added to the patient's list." + ANSI_RESET);
    } else {
      System.out.println(ANSI_RED + "Prescription couldn't be added to the patient's list." + ANSI_RESET);
    }

    if (d.getListe().find(p.getIdentificationNumber()).
    getIdentificationNumber()==position) {
      System.out.println(ANSI_GREEN + "Prescription was successfully added to the doctor's list."+ ANSI_RESET);
    } else {
      System.out.println(ANSI_RED + "Prescription couldn't be added to the doctor's list." + ANSI_RESET);
    }
  }


  /**
  *Use prescription to get a drug
  *@param prescriptions List with prescriptions
  *@param patients List with patients
  */
  public static void usePrescription(EnkelReseptListe prescriptions, Tabell<Patient> patients) {
    System.out.print("Patient's unique number: ");
    Scanner in = new Scanner(System.in);
    int patientId = Integer.parseInt(in.nextLine());
    if (patients.get(patientId)==null) {
      throw new DrugsException(ANSI_RED +"Patient couldn't be found." + ANSI_RESET);
    }
    System.out.print("Prescriptions unique number: ");
    int pNumber = Integer.parseInt(in.nextLine());
    Prescription p = patients.get(patientId).getListe().find(pNumber);
    if (p==null) {
      throw new DrugsException(ANSI_RED + "This person doesn't have the prescription." + ANSI_RESET);
    }

    if (!p.isValid()) {
      throw new DrugsException(ANSI_RED + "Prescription is not valid." + ANSI_RESET);
    }

    System.out.println("**************Prescription***************");
    System.out.println("Patient's name: "+ patients.get(patientId).getName());
    System.out.println("Doctors name: " +  p.getDoctorsName());
    System.out.println("Reit: " + p.getReit());
    if (p.isBlue()) {
      System.out.println("Price: 0 NOK ");
    } else {
      System.out.println("Price: " + p.getDrug().getPrice());
    }
    Drug d = (Drug) p.getDrug();
    System.out.println("Drug's unique number: " + d.getIdentificationNumber());
    System.out.println("Drug: " + d.getName());
    if (d instanceof Pill) {
      Pill pp = (Pill) d;
      System.out.println("Number of pills: " + pp.getNumberOfPills());
      System.out.println("Total amount of active substance: " + pp.getActiveSubstance()*
      pp.getNumberOfPills());
      p.setReit(p.getReit()-1);
      System.out.println("Prescription has been used.");
      System.out.println("Number of usage left: " + p.getReit());
    } else if(d instanceof Mixture) {
      Mixture m = (Mixture) d;
      System.out.println("Volume: " + m.getVolume());
      System.out.println("Total amount of active substance: " + m.getVolume()*
      m.getActiveSubstance());
      p.setReit(p.getReit()-1);
      System.out.println(ANSI_GREEN + "Prescription has been used." + ANSI_RESET);
      System.out.println("Number of usage left: " + p.getReit());
    }
  }


  /**
  *Printing all the blue prescriptions for the specific person
  *@param patients List with patients
  */
  public static void getBlue(Tabell<Patient> patients) {
    System.out.println("--------------------------------------------------------------");
    System.out.println("                  Blue prescriptions                          ");
    System.out.println("--------------------------------------------------------------");
    Scanner in = new Scanner(System.in);
    boolean blue = false;
    System.out.print("Unique patient number: ");
    int identificationNum = in.nextInt();
    Patient p = null;
    try {
      p = patients.get(identificationNum);
    } catch (IndexOutOfBoundsException e) {
      System.out.println(ANSI_RED + "Couldn't be found." + ANSI_RESET);
    }
    System.out.println(p.getName());
    EnkelReseptListe liste = p.getListe();
    System.out.println("--------------------------------------------------------------");
    System.out.println("Nr, Hvit/blaa, Unique patient number, Doctor's name, Drug number, Reit");
    System.out.println("--------------------------------------------------------------");
    Iterator it = liste.iterator();
    while(it.hasNext()) {
      Prescription pr = (Prescription) it.next();
      if (pr.isBlue()) {
        blue = true;
        System.out.println(pr.getIdentificationNumber() + ", blaa ," +
        pr.getPatientsNum() + ", " + pr.getDoctorsName() + ", " +
        pr.getDrug().getIdentificationNumber() + ", " + pr.getReit());
      }
    }
    if (blue == false) {
      System.out.println(p.getName() + " doesn't have any blue prescriptions.");
    }
  }

  /**
  *Printing how many prescriptions with addictive drugs there are and
  * how many of them are prescribed for those who live in Oslo
  *@param prescriptions List with prescriptions
  *@param patients List with patients
  */
  public static void addictiveInfo(EnkelReseptListe prescriptions, Tabell<Patient>
  patients) {
    int antall = 0;
    int oslo = 0;
    Iterator pr = prescriptions.iterator();
    while(pr.hasNext()) {
      Prescription p = (Prescription) pr.next();
      if (p.getDrug().isTypeB()) {
        Addictive drug = (Addictive) p.getDrug();
        antall++;
        Patient patient = (Patient) patients.find(p.getPatientsNum());
        if (Integer.parseInt(patient.getIndex())>=0001 &&
        Integer.parseInt(patient.getIndex())<=1295) {
          oslo++;
        }
      }
    }
    System.out.println("--------------------------------------------------------------");
    System.out.println("Total number of pescriptions with addictive drugs: " + antall);
    System.out.println("Prescribed to patients who live in Oslo: " + oslo);
    System.out.println("--------------------------------------------------------------");
  }


  /**
  *Printing all the prescriptions for specific doctor for mixtures.
  *And the amount of active substance for all medicine prescribed by this doctor
  * and how much of it in pills and mixtures
  *@param doctors List with doctors
  */
  public static void doctorsInfo(SortertEnkelListe doctors) {
    boolean mixd = false;
    double activeSubstancePills = 0;
    double activeSubstanceMixtures = 0;
    Scanner in = new Scanner(System.in);
    System.out.print("Doctor's name: ");
    String doctorsName = in.nextLine();
    Doctor d = (Doctor) doctors.find(doctorsName);
    if (d == null) {
      throw new DrugsException(ANSI_RED + "Doctor couldn't be found." + ANSI_RESET);
    }
    System.out.println("--------------------------------------------------------------");
    System.out.println(d.getName() + " prescriptions for the mixtures");
    System.out.println("--------------------------------------------------------------");

    Iterator p = d.getListe().iterator();
    while (p.hasNext()) {
      Prescription pr = (Prescription) p.next();
      if (pr.getDrug().isPill()) {
        Pill pill = (Pill) pr.getDrug();
        int numberOfPills = pill.getNumberOfPills();
        //If reit is 1, I would count it as 1 instead
        if(pr.getReit()!=0) {
          activeSubstancePills = activeSubstancePills + (pill.getActiveSubstance()*
          numberOfPills* pr.getReit());
        } else {
          activeSubstancePills = activeSubstancePills + (pill.getActiveSubstance()*
          numberOfPills* 1);
        }
      }

      if (pr.getDrug().isMixture()) {
        mixd = true;
        Mixture m = (Mixture) pr.getDrug();
        Double volume = m.getVolume();
        if (pr.getReit()!=0) {
          activeSubstanceMixtures = activeSubstanceMixtures + (volume *
          m.getActiveSubstance()* pr.getReit());
        } else {
          activeSubstanceMixtures = activeSubstanceMixtures + (volume *
          m.getActiveSubstance()* 1);
        }
        pr.info();
      }
    }
    if (!mixd) {
      System.out.println("Didn't prescribe any mixtures.");
    }
    System.out.println("--------------------------------------------------------------");
    System.out.println("Total amount of active substance for all prescriptions: "+
    (activeSubstancePills+activeSubstanceMixtures));
    System.out.println("Total amount of active substance for mixtures:" +
    activeSubstanceMixtures);
    System.out.println("Total amount of actuve substance for pills: " +
    activeSubstancePills);
    System.out.println("--------------------------------------------------------------");
  }

  /**
  *Printing all the doctors who has prescribed opioids and how many of
  *those prescriptions they have.
  *Printing all the patients who has been prescribed opioids and how many
  *those prescriptions they have.
  *@param doctors List with doctors
  *@param patients List with patients
  */
  public static void opioidsInfo(SortertEnkelListe doctors,Tabell<Patient> patients) {
    System.out.println("------------------------------------------------------------");
    System.out.println("------------------------------------------------------------");
    boolean doctorr = false;
    Iterator doc = doctors.iterator();
    while(doc.hasNext()) {
      Doctor d = (Doctor) doc.next();
      int numberOpioid = 0;
      Iterator pr = d.getListe().iterator();
      while (pr.hasNext()) {
        Prescription p = (Prescription) pr.next();
        if (p.getDrug().isTypeA())  {
          numberOpioid++;
        }
      }
      if (numberOpioid>0) {
        System.out.println(d.getName() + " has " + numberOpioid + " prescriptions with opioids.");
        doctorr = true;
      }
    }
    if (doctorr == false) {
      System.out.println("There are no doctors who prescribed opioids.");
    }

    System.out.println("------------------------------------------------------------");
    System.out.println("------------------------------------------------------------");
    boolean people = false;
    Iterator pat = patients.iterator();
    while (pat.hasNext()) {
      Patient patient = (Patient) pat.next();
      int numberOp = 0;
      Iterator presc = patient.getListe().iterator();
      while (presc.hasNext()) {
        Prescription prr = (Prescription) presc.next();
        Drug opioidper = (Drug) prr.getDrug();
        if (opioidper.isTypeA()) {
          numberOp++;
        }
      }
      if (numberOp>0) {
        System.out.println(patient.getName() + " has " + numberOp +
        " prescriptions with opioids.");
        people = true;
      }
    }
    if (people==false) {
      System.out.println("No one has prescriptions with opioids.");
    }
    System.out.println("------------------------------------------------------------");
  }

  /**
  *Writing to the file
  *@param prescriptions List with prescriptions
  *@param patients List with patients
  *@param drugs List with drugs
  *@param doctors List with doctors
  */
  public static void writeToFile(EnkelReseptListe prescriptions,
  Tabell<Patient> patients, Tabell<Drug> drugs, SortertEnkelListe doctors) {

    try {

      File file = new File("updated.txt");
      if (!file.exists()) {
        file.createNewFile();
      }

      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      //passing the stream to the file instead
      FileOutputStream f = new FileOutputStream(file);
      System.setOut(new PrintStream(f));
      //Patients
      System.out.println("# Personer (nr, navn, fnr, adresse, postnr)");
      printPatients(patients,0);
      System.out.println("\n# Legemidler (nr, navn, form, type, pris, antall/mengde, virkestoff [, styrke])");
      printDrugs(drugs,0);
      System.out.println("\n# Leger (navn, avtalenr / 0 hvis ingen avtale)");
      printDoctors(doctors,0);
      System.out.println("\n# Resepter (nr, hvit/blae, persNummer, legeNavn, legemiddelNummer, reit)");
      printPrescriptions(prescriptions,0);
      System.out.println("\n# Slutt");
      bw.close();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //stream is back to default
      System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
  }
}
