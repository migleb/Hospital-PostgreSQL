import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DBHelper {
	
	private static final String LOG_HEADER = DBHelper.class.getName() + " : " ;
	private static Connection dbConnection;
	
	public static void initialize() {
		DBHelper.close();
		try {
			Class.forName("org.postgresql.Driver");
			dbConnection = DriverManager.getConnection("jdbc:postgresql://pgsql2.mif/studentu","mibu1254","mibu1254");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void newPatient(String AK, String name, String surname, String city, String street, String building, String apartment, Integer doctorID, String codeIllness, String title) {
		PreparedStatement insertPatient = null;
		PreparedStatement insertIllness = null;
		try {
			dbConnection.setAutoCommit(false);
			insertPatient = dbConnection.prepareStatement("INSERT INTO Pacientas VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			insertIllness = dbConnection.prepareStatement("INSERT INTO Liga VALUES (?, ?)");
			
			insertPatient.setString(1, AK);
			insertPatient.setString(2, name);
			insertPatient.setString(3, surname);
			insertPatient.setString(4, city);
			insertPatient.setString(5, street);
			insertPatient.setString(6, building);
			insertPatient.setString(7, apartment);
			insertPatient.setInt(8, doctorID);
			if (title != null) {
				insertIllness.setString(1, codeIllness);
				insertIllness.setString(2, title);
				insertIllness.executeUpdate();
			}
			insertPatient.setString(9, codeIllness);
			insertPatient.executeUpdate();
			
			dbConnection.commit();
		}catch (SQLException e){
			try {
				dbConnection.rollback();
			}catch (SQLException er){
				
			}
			System.out.println("Error occured creating new patient");
		}finally {
			try {
				if (insertPatient != null){
					insertPatient.close();
				}
				dbConnection.setAutoCommit(true);
			}catch (SQLException e){
				
			}
		}
	}
	
	public static void newAppointment(String date, Integer doctorID, String patientAK) {
		PreparedStatement addAppointment = null;
		try {
			dbConnection.setAutoCommit(false);
			addAppointment = dbConnection.prepareStatement("INSERT INTO mibu1254.Vizitas (Data,Gydytojo_ID,Paciento_AK) VALUES (CAST(? as date), ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			addAppointment.setString(1, date);
			addAppointment.setInt(2, doctorID);
			addAppointment.setString(3, patientAK);
			addAppointment.executeUpdate();
			
			ResultSet rs = addAppointment.getGeneratedKeys();
			int appointment = 0;
			if (rs.next()){
				appointment = rs.getInt(1);
			}else {
				throw new SQLException("Error retrieving generated keys");
			}
			
			dbConnection.commit();
		}catch (SQLException e){
			try {
				dbConnection.rollback();
			}catch (SQLException er){
				
			}
			System.out.println("Error occured creating new appointment");
			e.printStackTrace();
		}finally {
			try {
				if (addAppointment != null){
					addAppointment.close();
				}
				dbConnection.setAutoCommit(true);
			}catch (SQLException e){
				
			}
		}
	}
	
	public static void editVisit(Integer id, String date) {
		PreparedStatement updateVisit = null;
		try {
			dbConnection.setAutoCommit(false);
			updateVisit = dbConnection.prepareStatement("UPDATE Vizitas SET data = CAST(? as date) WHERE ID = " + Integer.toString(id));
			updateVisit.setString(1, date);
			updateVisit.executeUpdate();
			
			dbConnection.commit();
			
		}catch (SQLException e){
			try {
				dbConnection.rollback();
			}catch (SQLException err){
			}
			//System.out.println(LOG_HEADER + "Error occured editing category");
			e.printStackTrace();
		}finally {
			try {
				if (updateVisit != null){
					updateVisit.close();
				}
			}catch (SQLException e){
			}
		}
	}
	
	
	public static void deleteVisit(Integer id) {
		try {
			Statement visitRet = dbConnection.createStatement();
			ResultSet visit = visitRet.executeQuery("DELETE FROM Vizitas WHERE ID = " + Integer.toString(id));

		}catch (SQLException e){
			System.out.println(LOG_HEADER + "Error occured removing appointment");
		}
	}
	
	public static List<HashMap<String, String>> getDoctorList(String query) {
		List<HashMap<String,String>> doctorList = new ArrayList<HashMap<String,String>>();
		try {
			Statement doctorsRet = dbConnection.createStatement();
			ResultSet doctors = null;
			if (query == "") {
				doctors = doctorsRet.executeQuery("SELECT * FROM Gydytojas");
			} else {
				doctors = doctorsRet.executeQuery(query);
			}
			while(doctors.next()) {
				String ID = doctors.getString("ID");
				String name = doctors.getString("vardas");
				String surname = doctors.getString("pavarde");
				String birth = doctors.getString("gimimas");
				String position = doctors.getString("pareigos");
				String email = doctors.getString("el_pastas");
				String phone = doctors.getString("tel_numeris");
				
				
				HashMap<String, String> doctor = new HashMap<String, String>();
				doctor.put("ID", ID);
				doctor.put("name", name);
				doctor.put("surname", surname);
				doctor.put("birth", birth);
				doctor.put("position", position);
				doctor.put("email", email);
				doctor.put("phone", phone);
				
				doctorList.add(doctor);
			}
			
		} catch (SQLException e){
			System.out.println(LOG_HEADER + "Error occured retrieving articles list");
		}
		for (HashMap<String,String> list : doctorList) {
			System.out.println("\t" + list.get("ID") + " " + list.get("name") + " " + list.get("surname") + " " + list.get("birth") + " " + list.get("position"));
		}
		return doctorList;
	}
	
	public static List<HashMap<String, String>> getPatientList(String query) {
		List<HashMap<String,String>> patientList = new ArrayList<HashMap<String,String>>();
		try {
			Statement patientsRet = dbConnection.createStatement();
			ResultSet patients = null;
			if (query == "") {
					patients = patientsRet.executeQuery("SELECT * FROM Pacientas");
			} else {
				patients = patientsRet.executeQuery("SELECT * FROM Pacientas WHERE gydytojo_id = " + query);
			}
			while(patients.next()) {
				String AK = patients.getString("AK");
				String name = patients.getString("vardas");
				String surname = patients.getString("pavarde");
				String city = patients.getString("miestas");
				String street = patients.getString("gatve");
				String building = patients.getString("namas");
				String apartment = patients.getString("butas");
				String doctorID = patients.getString("Gydytojo_ID");
				String codeIllness = patients.getString("Ligos_kodas");
				
				HashMap<String, String> patient = new HashMap<String, String>();
				patient.put("AK", AK);
				patient.put("name", name);
				patient.put("surname", surname);
				patient.put("city", city);
				patient.put("street", street);
				patient.put("building", building);
				patient.put("apartment", apartment);
				patient.put("doctorID", doctorID);
				patient.put("codeIllness", codeIllness);
				
				patientList.add(patient);
			}
		} catch (SQLException e){
			System.out.println(LOG_HEADER + "Error occured retrieving articles list");
		}
		for (HashMap<String,String> list : patientList) {
			System.out.println("\t" + list.get("AK") + " " + list.get("name") + " " + list.get("surname") + " " + list.get("city") + " " + list.get("doctorID") + " " + list.get("codeIllness"));
		}
		return patientList;
	}
	
	public static List<HashMap<String, String>> getAppointmentList(String query) {
		List<HashMap<String,String>> appointmentList = new ArrayList<HashMap<String,String>>();
		try {
			Statement appointmentsRet = dbConnection.createStatement();
			ResultSet appointments = appointmentsRet.executeQuery("SELECT * FROM Vizitas");
			if (query == "") {
				while(appointments.next()) {
					String id = appointments.getString("id");
					String reason = appointments.getString("priezastis");
					String date = appointments.getString("data");
					String doctorID = appointments.getString("gydytojo_id");
					String patientAK = appointments.getString("paciento_ak");
					
					HashMap<String, String> appointment = new HashMap<String, String>();
					appointment.put("id", id);
					appointment.put("reason", reason);
					appointment.put("date", date);
					appointment.put("doctorID", doctorID);
					appointment.put("patientAK", patientAK);
					
					appointmentList.add(appointment);
				}
			} else {
				while (appointments.next()) {
					String id = appointments.getString("id");
					String reason = appointments.getString("priezastis");
					String date = appointments.getString("data");
					String doctorID = appointments.getString("gydytojo_id");
					String patientAK = appointments.getString("paciento_ak");
					if (query == doctorID) {
						HashMap<String, String> appointment = new HashMap<String, String>();
						appointment.put("id", id);
						appointment.put("reason", reason);
						appointment.put("date", date);
						appointment.put("doctorID", doctorID);
						appointment.put("patientAK", patientAK);
						
						appointmentList.add(appointment);
					}
				}
			}
		} catch (SQLException e){
			System.out.println(LOG_HEADER + "Error occured retrieving articles list");
		}
		for (HashMap<String,String> list : appointmentList) {
			System.out.println("\t" + list.get("id") + " " + list.get("date") + " " + list.get("doctorID") + " " + list.get("patientAK"));
		}
		return appointmentList;
	}
	
	public static List<HashMap<String, String>> getCodeList(String query) {
		List<HashMap<String,String>> codeList = new ArrayList<HashMap<String,String>>();
		try {
			Statement codesRet = dbConnection.createStatement();
			ResultSet codes = null;
			if (query == "") {
				codes = codesRet.executeQuery("SELECT * FROM Liga");
				while(codes.next()) {
					String no = codes.getString("kodas");
					String title = codes.getString("pavadinimas");
					
					HashMap<String, String> code = new HashMap<String, String>();
					code.put("code", no);
					code.put("title", title);
					
					codeList.add(code);
				}
			} else {
				codes = codesRet.executeQuery("SELECT * FROM Liga");
				while (codes.next()) {
					String no = codes.getString("kodas");
					String title = codes.getString("pavadinimas");
					if (title.toLowerCase().contains(query.toLowerCase())) {
						HashMap<String, String> code = new HashMap<String, String>();
						code.put("code", no);
						code.put("title", title);
						
						codeList.add(code);
					}
				}
			}
		} catch (SQLException e){
			System.out.println(LOG_HEADER + "Error occured retrieving articles list");
		}
		for (HashMap<String,String> list : codeList) {
			System.out.println("\t" + list.get("code") + " " + list.get("title"));
		}
		return codeList;
	}
	
	public static void close() {
		if (dbConnection != null){
			try {
				dbConnection.close();
			}catch (SQLException e){
			}
		}
	}
	
	

}
