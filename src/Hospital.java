import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Hospital {

	public static void main(String[] args) throws SQLException {
		DBHelper.initialize();
		
		int instruction = -1;
		
		while (instruction != 0){
			System.out.println("\t MENU:");
			System.out.println("\t 1 : List of doctors");
			System.out.println("\t 2 : List of patients");
			System.out.println("\t 3 : List of illness");
			System.out.println("\t 4 : List of appointments");
			System.out.println("\t 5 : Edit appointment");
			System.out.println("\t 6 : Delete appointment");
			System.out.println("\t 7 : New patient");
			System.out.println("\t 8 : New appointment");
			System.out.println("\t 0 : Exit");
			
			Scanner scanner = new Scanner(System.in);
			try {
				instruction = scanner.nextInt();
			} catch (InputMismatchException e){
				scanner.nextLine();
				instruction = -1;
			}
			switch (instruction) {
				case 1 : {
					DBHelper.getDoctorList("");
				}
				break;
				case 2 : {
					System.out.println("\t MENU:");
					System.out.println("\t 1 : List of patients with specific doctor ID");
					System.out.println("\t 2 : List of all patients");
					Scanner scan = new Scanner(System.in);
					int choice = -1;
					try {
						choice = scanner.nextInt();
					} catch (InputMismatchException e){
						scanner.nextLine();
						choice = -1;
					}
					switch (choice) {
						case 1 : {
							System.out.print("ID: ");
							scan = new Scanner(System.in);
							DBHelper.getPatientList(scan.nextLine());
						}
						break;
						case 2 : {
							DBHelper.getPatientList("");
						}
						break;
					}
				}
				break;
				case 3 : {
					System.out.println("\t MENU:");
					System.out.println("\t 1 : List of illnesses by the name or part of it");
					System.out.println("\t 2 : List of all illnesses");
					Scanner scan = new Scanner(System.in);
					int choice = -1;
					try {
						choice = scanner.nextInt();
					} catch (InputMismatchException e){
						scanner.nextLine();
						choice = -1;
					}
					switch (choice) {
						case 1 : {
							System.out.print("Name: ");
							scan = new Scanner(System.in);
							DBHelper.getCodeList(scan.nextLine());
						}
						break;
						case 2 : {
							DBHelper.getCodeList("");
						}
						break;
					}
				}
				break;
				case 4 : {
					System.out.println("\t MENU:");
					System.out.println("\t 1 : List of appointments by specific doctor");
					System.out.println("\t 2 : List of all appointments");
					Scanner scan = new Scanner(System.in);
					int choice = -1;
					try {
						choice = scanner.nextInt();
					} catch (InputMismatchException e){
						scanner.nextLine();
						choice = -1;
					}
					switch (choice) {
						case 1 : {
							System.out.println("ID: ");
							scan = new Scanner(System.in);
							DBHelper.getAppointmentList(scan.nextLine());
						}
						break;
						case 2 : {
							DBHelper.getAppointmentList("");
						}
						break;
					}
				}
				break;
				case 5 : {
					System.out.println("Choose the ID of the appointment you want to edit");
					DBHelper.getAppointmentList("");
					System.out.print("ID: ");
					Scanner scan = new Scanner(System.in);
					int id = Integer.parseInt(scan.nextLine());
					System.out.print("Date (format yyyy-mm-dd): ");
					String date = scan.nextLine();
					DBHelper.editVisit(id, date);
					
				}
				break;
				case 6 : {
					System.out.println("Choose the ID of the appointment you want to delete");
					DBHelper.getAppointmentList("");
					System.out.print("ID: ");
					Scanner scan = new Scanner(System.in);
					int id = scan.nextInt();
					DBHelper.deleteVisit(id);
				}
				break;
				case 7 : {
					System.out.print("Insert person id: ");
					Scanner scan = new Scanner(System.in);
					String AK = scan.nextLine();
					System.out.print("Insert name: ");
					String name = scan.nextLine();
					System.out.print("Insert surname: ");
					String surname = scan.nextLine();
					System.out.print("Insert city: ");
					String city = scan.nextLine();
					System.out.print("Insert street: ");
					String street = scan.nextLine();
					System.out.print("Insert building: ");
					String building = scan.nextLine();
					System.out.print("Insert apartment: ");
					String apartment = scan.nextLine();
					DBHelper.getDoctorList("");
					System.out.print("Choose doctor id: ");
					int doctorID = Integer.parseInt(scan.nextLine());
					System.out.println("\t 1 : if you want to choose the illness from the list");
					System.out.println("\t 2 : if you want to add new illness to the list");
					int inst = Integer.parseInt(scan.nextLine());
					switch(inst) {
						case 1 : {
							DBHelper.getCodeList("");
							System.out.print("Insert the code: ");
							String code = scan.nextLine();
							DBHelper.newPatient(AK, name, surname, city, street, building, apartment, doctorID, code, null);
						}
						break;
						case 2 : {
							System.out.println("Insert code of illness: ");
							String code = scan.nextLine();
							System.out.print("Insert name of illness: ");
							String title = scan.nextLine();
							DBHelper.newPatient(AK, name, surname, city, street, building, apartment, doctorID, code, title);
						}
						break;
					}
				}
				break;
				case 8 : {
					System.out.print("Insert date (format yyyy-mm-dd): ");
					Scanner scan = new Scanner(System.in);
					String date = scan.nextLine();
					System.out.print("Insert doctor ID: ");
					int doctorID = Integer.parseInt(scan.nextLine());
					DBHelper.getPatientList(Integer.toString(doctorID));
					System.out.print("Insert patient AK: ");
					String patientAK = scan.nextLine();
					DBHelper.newAppointment(date, doctorID, patientAK);
				}
			}
		}

	}

}
