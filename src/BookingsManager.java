
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;


public class BookingsManager {
    File bookingsFile;
    NodeList bookings;

    public static void main(String[] args) {
        BookingsManager bm = new BookingsManager();

        bm.loadBookingsFile();
        //bm.showBookingByID(2);
    }

    public void addNewBooking(){
        Scanner input = new Scanner(System.in);
        Document doc = createParsedDocument(bookingsFile);
        Element rootElement, bookingElement, clientElement, agencyElement, priceElement, roomElement, hotelElement, checkinElement, nightsElement;
        String client, agency, price, room, hotel, checkin, nights;

        // Blank elements are created
        rootElement = doc.getDocumentElement();
        bookingElement = doc.createElement("booking");
        clientElement = doc.createElement("client");
        agencyElement = doc.createElement("agency");
        priceElement = doc.createElement("price");
        roomElement = doc.createElement("room");
        hotelElement = doc.createElement("hotel");
        checkinElement = doc.createElement("check_in");
        nightsElement = doc.createElement("room_nights");

        // <CLIENT>
        System.out.print("Please, enter your name: ");
        client = input.nextLine();
        clientElement.appendChild(doc.createTextNode(client));
        clientElement.setAttribute("id_client", Double.toString((int) Math.random()*1000));

        // <AGENCY>
        System.out.print("Please, enter your agency: ");
        agency = input.nextLine();
        agencyElement.appendChild(doc.createTextNode(agency));
        agencyElement.setAttribute("id_agency", Double.toString((int) Math.random()*1000));

        // <PRICE>
        priceElement.appendChild(doc.createTextNode(Double.toString(Math.random()*1000)));

        // <ROOM>
        System.out.print("Please, enter the room type (1: Double | 2: Apartament | 3: Individual | 4. Suite): ");
        try   {
            room = input.nextLine();
            rootElement.appendChild(doc.createTextNode(client));
            roomElement.setAttribute("id_client", Double.toString((int) Math.random()*1000));

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // <HOTEL>
        System.out.print("Please, enter the hotel name: ");
        hotel = input.nextLine();

        // <CHECK_IN>

        // <NIGHTS>
        System.out.print("Please, enter the amount of nights: ");
        nights = input.nextLine();
    }

    public void showBookingByID(int bID) {
        Boolean bookingFound = false;
        int i = 0;
        Node node;
        Element element;

        try {
            while (!bookingFound) {
                node = bookings.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) node;
                    if (element.getAttribute("location_number").equals(String.valueOf(bID))) {
                        printBookingInfo(element);
                        bookingFound = true;
                    }
                }
                i++;
            }
        }
        catch (NullPointerException e){
            System.out.println("ERROR - Booking with ID: '" + bID + "' has not been found. Please, enter a valid number.");
        }
    }

    public void printBookingInfo(Element element) {
        System.out.println();
        System.out.println("============= Booking " + element.getAttribute("location_number") + " ==============");
        System.out.println("Booking ID: " + element.getAttribute("location_number"));
        System.out.println("Name: " + element.getElementsByTagName("client").item(0).getTextContent());
        System.out.println("Agency: " + element.getElementsByTagName("agency").item(0).getTextContent());
        System.out.println("Price: " + element.getElementsByTagName("price").item(0).getTextContent());
        System.out.println("Room: " + element.getElementsByTagName("room").item(0).getTextContent());
        System.out.println("Hotel: " + element.getElementsByTagName("hotel").item(0).getTextContent());
        System.out.println("Check-in: " + element.getElementsByTagName("check_in").item(0).getTextContent());
        System.out.println("Nights: " + element.getElementsByTagName("room_nights").item(0).getTextContent());
    }

    public void loadBookingsFile() {
        try {
            bookingsFile = new File("src/bookings.xml");
            Document doc = createParsedDocument(bookingsFile);

            bookings = doc.getElementsByTagName("booking");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Document createParsedDocument(File file) {
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            return doc;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String checkRoomType (String roomType) {

        if (roomType == "1") {
            return "Double";
        }
        else if (roomType == "2") {
            return "Apartament";
        }
        else if (roomType == "3") {
            return "Individual";
        }
        else if (roomType == "4") {
            return "Suite";
        }
        else {
            "El "
        }

    }


    public File getBookingsFile() {
        return bookingsFile;
    }
    public void setBookingsFile(File bookingsFile) {
        this.bookingsFile = bookingsFile;
    }
    public NodeList getBookings() {
        return bookings;
    }
    public void setBookings(NodeList bookings) {
        this.bookings = bookings;
    }
}