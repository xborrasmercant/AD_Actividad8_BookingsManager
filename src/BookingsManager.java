
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BookingsManager {
    File bookingsFile;
    NodeList bookings;

    public static void main(String[] args) {
        BookingsManager bm = new BookingsManager();
        Scanner input = new Scanner(System.in);
        String option;

        bm.loadBookingsFile();

        while (true) {
            System.out.println();
            System.out.println("===============================");
            System.out.println("1. Show existing booking");
            System.out.println("2. Add new booking");
            System.out.println("3. Delete existing booking");
            System.out.println("4. Modify existing booking");
            System.out.println("5. Exit");
            System.out.println("===============================");

            System.out.print("Enter an option please (1-5): ");
            option = input.nextLine();

                switch (option) {
                    case "1":
                        bm.showBookingByID(input);
                        break;
                    case "2":
                        bm.addNewBooking();
                        break;
                    case "3":
                        bm.deleteBookingByID(input);
                        break;
                    case "4":
                        bm.modifyBookingByID(input);
                        break;
                    case "5":
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Please, try again.");
                }
        }
    }

    public void modifyBookingByID(Scanner input) {
        Element rootElement = null, bookingElement = null, clientElement = null, agencyElement = null, priceElement = null, roomElement = null, hotelElement = null, checkinElement = null, nightsElement = null;
        String client, agency, roomVal, roomId, hotel, nights;
        String[] room_info;
        String bID;
        Boolean bookingFound = false;
        int i = 0;
        Node bookingNode = null;

        System.out.print("Enter the id from the booking you wish to modify: ");
        bID = input.nextLine();
        Document doc = createParsedDocument(bookingsFile);
        bookings = doc.getElementsByTagName("booking");

        while (!bookingFound) {
            bookingNode = bookings.item(i);
            if (bookingNode != null) {
                if (bookingNode.getNodeType() == Node.ELEMENT_NODE) {
                    bookingElement = (Element) bookingNode;
                    if (bookingElement.getAttribute("location_number").equals(String.valueOf(bID))) {

                        clientElement = (Element) bookingElement.getElementsByTagName("client").item(0);
                        agencyElement = (Element) bookingElement.getElementsByTagName("agency").item(0);
                        priceElement = (Element) bookingElement.getElementsByTagName("price").item(0);
                        roomElement = (Element) bookingElement.getElementsByTagName("room").item(0);
                        hotelElement = (Element) bookingElement.getElementsByTagName("hotel").item(0);
                        nightsElement = (Element) bookingElement.getElementsByTagName("room_nights").item(0);

                        // <CLIENT>
                        System.out.println();
                        System.out.print("Please, enter your name: ");
                        client = input.nextLine();
                        clientElement.setTextContent(client);

                        // <AGENCY>
                        System.out.println();
                        System.out.print("Please, enter your agency: ");
                        agency = input.nextLine();
                        agencyElement.setTextContent(agency);

                        // <PRICE>
                        priceElement.appendChild(doc.createTextNode((int) (Math.random() * 1000) + ",0"));

                        // <ROOM>
                        System.out.println();
                        System.out.print("Please, enter the room type ");
                        room_info = addRoomType(input);
                        roomVal = room_info[1];
                        roomElement.setTextContent(roomVal);

                        // <HOTEL>
                        System.out.println();
                        System.out.print("Please, enter the hotel name: ");
                        hotel = input.nextLine();
                        hotelElement.setTextContent(hotel);

                        // <NIGHTS>
                        System.out.println();
                        System.out.print("Please, enter the amount of nights: ");
                        nights = input.nextLine();
                        nightsElement.setTextContent(nights);

                        bookingFound = true;
                    }
                }
                i++;
            } else {
                System.out.println("ERROR - Booking with ID: '" + bID + "' has not been found. Please, enter a valid id.");
                return;
            }
        }

        // UPDATING THE XML FILE
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Enable indentation
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(bookingsFile);
            transformer.transform(source, result);
            trimWhitespaceFromXML();

            System.out.println("Your booking with ID '" + bID + "' has been modified successfully!");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBookingByID(Scanner input) {
        String bID;
        Boolean bookingFound = false;
        int i = 0;
        Node bookingNode = null;
        Element bookingElement, rootElement;


        System.out.print("Enter the id from the booking you wish to delete: ");
        bID = input.nextLine();
        Document doc = createParsedDocument(bookingsFile);
        rootElement = doc.getDocumentElement();
        bookings = doc.getElementsByTagName("booking");

        while (!bookingFound) {
            bookingNode = bookings.item(i);
            if (bookingNode != null) {
                if (bookingNode.getNodeType() == Node.ELEMENT_NODE) {
                    bookingElement = (Element) bookingNode;
                    if (bookingElement.getAttribute("location_number").equals(String.valueOf(bID))) {
                        rootElement.removeChild(bookingNode);
                        bookingFound = true;
                    }
                }

                i++;
            }
            else {
                System.out.println("ERROR - Booking with ID: '" + bID + "' has not been found. Please, enter a valid id.");
                return;
            }
        }


        // UPDATING THE XML FILE
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Enable indentation
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(bookingsFile);
            transformer.transform(source, result);
            trimWhitespaceFromXML();

            System.out.println("Booking with ID: " + bID + " has been deleted successfully!");
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void addNewBooking(){
        Scanner input = new Scanner(System.in);
        Document doc = createParsedDocument(bookingsFile);
        Element rootElement, bookingElement, clientElement, agencyElement, priceElement, roomElement, hotelElement, checkinElement, nightsElement;
        String client, agency, roomVal, roomId, hotel, nights;
        String[] room_info;

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
        System.out.println();
        System.out.print("Please, enter your name: ");
        client = input.nextLine();
        clientElement.appendChild(doc.createTextNode(client));
        clientElement.setAttribute("id_client", Integer.toString((int) (Math.random() * 1000)));

        // <AGENCY>
        System.out.println();
        System.out.print("Please, enter your agency: ");
        agency = input.nextLine();
        agencyElement.appendChild(doc.createTextNode(agency));
        agencyElement.setAttribute("id_agency", Integer.toString((int) (Math.random() * 1000)));

        // <PRICE>
        priceElement.appendChild(doc.createTextNode((int) (Math.random() * 1000) + ",0"));

        // <ROOM>
        System.out.println();
        System.out.print("Please, enter the room type ");
        room_info = addRoomType(input);
        roomVal = room_info[1];
        roomId = room_info[0];
        roomElement.appendChild(doc.createTextNode(roomVal));
        roomElement.setAttribute("id_type", roomId);

        // <HOTEL>
        System.out.println();
        System.out.print("Please, enter the hotel name: ");
        hotel = input.nextLine();
        hotelElement.appendChild(doc.createTextNode(hotel));
        agencyElement.setAttribute("id_hotel", Integer.toString((int) (Math.random() * 1000)));

        // <CHECK_IN>
        checkinElement.appendChild(doc.createTextNode("08/06/2018"));

        // <NIGHTS>
        System.out.println();
        System.out.print("Please, enter the amount of nights: ");
        nights = input.nextLine();
        nightsElement.appendChild(doc.createTextNode(nights));

        // BOOKING
        String booking_id = Integer.toString((int) (Math.random() * 10000));
        bookingElement.setAttribute("location_number", booking_id);
        bookingElement.appendChild(clientElement);
        bookingElement.appendChild(agencyElement);
        bookingElement.appendChild(priceElement);
        bookingElement.appendChild(roomElement);
        bookingElement.appendChild(hotelElement);
        bookingElement.appendChild(checkinElement);
        bookingElement.appendChild(nightsElement);

        // ROOT ELEMENT
        rootElement.appendChild(bookingElement);

        // UPDATING THE XML FILE
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Enable indentation
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(bookingsFile);
            transformer.transform(source, result);
            trimWhitespaceFromXML();

            System.out.println("Your booking with ID '" + booking_id + "' has been added successfully!");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void showBookingByID(Scanner input) {
        Boolean bookingFound = false;
        String bID;
        int i = 0;
        Node node;
        Element element;

        System.out.print("Enter the id from the booking you wish to show: ");
        bID = input.nextLine();

        Document doc = createParsedDocument(bookingsFile);
        bookings = doc.getElementsByTagName("booking");

        try {
            while (!bookingFound) {
                node = bookings.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) node;
                    if (element.getAttribute("location_number").equals(bID)) {
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
        System.out.println();
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

    public String[] addRoomType(Scanner input) {
        System.out.print("(1: Double | 2: Apartament | 3: Individual | 4. Suite): ");
        String roomType = input.nextLine();

        if (roomType.equals("1")) {
            return new String[]{roomType, "Double"};
        }
        else if (roomType.equals("2")) {
            return new String[]{roomType, "Apartament"};
        }
        else if (roomType.equals("3")) {
            return new String[]{roomType, "Individual"};
        }
        else if (roomType.equals("4")) {
            return new String[]{roomType, "Suite"};
        }
        else {
            System.out.println();
            System.out.print("Invalid input. Enter a valid room type. ");
            return addRoomType(input);
        }
    }

    private static void removeWhitespaceNodes(Element element) {
        NodeList children = element.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child instanceof Text && ((Text) child).getData().trim().isEmpty()) {
                element.removeChild(child);
            } else if (child instanceof Element) {
                removeWhitespaceNodes((Element) child);
            }
        }
    }

    public void trimWhitespaceFromXML() {
        Document doc = createParsedDocument(bookingsFile);

        try {
            doc.getDocumentElement().normalize();

            // Llama a la función recursiva para limpiar los nodos de texto en blanco
            removeWhitespaceNodes(doc.getDocumentElement());

            // Guarda los cambios en el archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(bookingsFile);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
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