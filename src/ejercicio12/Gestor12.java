package ejercicio12;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Gestor12 {
	Scanner sc = new Scanner(System.in);
	Alumno a1;
	Grupo g1;
	ArrayList<Alumno> aLista = new ArrayList<>();
	ArrayList<Grupo> gLista = new ArrayList<>();

	private static final String URL = "jdbc:mysql://localhost:3306/tarea12";
	private static final String USER = "root";
	private static final String PASS = "manager1";

	public void visualizarMenu() {
		System.out.println(
				"""

						Introducir la opcion deseada
							1. Insertar alumnos
							2. Insertar grupos
							3. Mostrar alumnos
							4. Guardar alumnos en fichero (.dat y .txt)
							5. Leer fichero y agregar a la BD
							6. Modificar el nombre a partir de su Primary Key
							7. Eliminar un alumno a partir de su Priamary Key
							8. Eliminar los alumnos del curso indicado por el usuario (mostrar previamente los cursos existentes)
							9. Guardar todos los grupos (con toda su información como atributos) en un fichero XML y JSON.
							10.Leer un fichero XML o JSON de grupos (con en formato anterior) y guardarlos en la BD
							0. Salir
						""");
	}

	public void menu() {
		int opcion;
		do {
			visualizarMenu();
			opcion = sc.nextInt();
			sc.nextLine();
			switch (opcion) {
			case 1:
				datosAlumno();
				break;
			case 2:
				datosGrupo();
				break;
			case 3:
				System.out.println("""
						1.Mostrar alumnos
						2.Solo grupos
						""");

				opcion = sc.nextInt();
				sc.nextLine();
				if (opcion == 1) {
					mostrarAlumnos();
				} else if (opcion == 2) {
					mostrarGrupos();
				}
				break;
			case 4:
				System.out.println("""
						1.Escribir binario
						2.Escribir texto
						""");
				opcion = sc.nextInt();
				sc.nextLine();
				if (opcion == 1) {
					ficheroBinario();
				} else if (opcion == 2) {
					escribirTxt();
				}

				break;
			case 5:
				leerbinario();
				break;
			case 6:
				modificarNombrePK();
				break;
			case 7:
				eliminarPK();
				break;
			case 8:
				eliminarCurso();
				break;
			case 9:
				System.out.println("""
						1.Escribir xml sin atributos
						2.Escribir xml con atributos
						""");
				opcion = sc.nextInt();
				sc.nextLine();
				if (opcion == 1) {
					escribirXML();
				} else if (opcion == 2) {
					escribirAtributos();
				}
				break;
			case 10:
				System.out.println("""
						1.Leer xml sin atributos
						2.Leer xml con atributos
						""");
				opcion = sc.nextInt();
				sc.nextLine();
				if (opcion == 1) {
					leerSinAtributos();
				} else if (opcion == 2) {
					leerXml();
				}
				break;
			case 0:

				opcion = 0;

				break;

			default:
				break;
			}
		} while (opcion != 0);

	}

	public void datosAlumno() {
		a1 = new Alumno();

		System.out.println("Introduzca el nia");
		a1.setNia(sc.nextInt());

		sc.nextLine();

		System.out.println("Introduzca el nombre");
		a1.setNombre(sc.nextLine());

		System.out.println("Introduzca el apellido");
		a1.setApellidos(sc.nextLine());

		System.out.println("Introduzca el genero");
		String genero = sc.nextLine();
		a1.setGenero(genero.charAt(0));

		System.out.println("Introduzca la fecha de nacimiento");
		String fecha = sc.nextLine();
		SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
		try {
			a1.setFechaNac(format.parse(fecha));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Introduzca el ciclo");
		a1.setCiclo(sc.nextLine());

		System.out.println("Introduzca el curso");
		a1.setCurso(sc.nextLine());

		aLista.add(a1);
		insertarAlumno(a1);
	}

	public void insertarAlumno(Alumno a1) {

		String sql = "INSERT INTO alumnos (nia, nombre, apellido, genero, fechaNac, ciclo, curso, id_grupo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setInt(1, a1.getNia());
			ps.setString(2, a1.getNombre());
			ps.setString(3, a1.getApellidos());
			ps.setString(4, String.valueOf(a1.getGenero()));

			java.sql.Date fechaSql = new java.sql.Date(a1.getFechaNac().getTime());
			ps.setDate(5, fechaSql);

			ps.setString(6, a1.getCiclo());
			ps.setString(7, a1.getCurso());
			System.out.println("A que grupo lo desea insertar");
			mostrarGrupos();

			int id = sc.nextInt();
			sc.nextLine();

			boolean grupoExiste = false;

			for (Grupo grupo : gLista) {
				if (grupo.getId() == id) {
					System.out.println("Si existe");
					ps.setInt(8, id);
					grupoExiste = true;
				}

			}

			int filas = ps.executeUpdate();
			if (filas > 0 && grupoExiste == true) {
				System.out.println("Alumno insertado");
			} else {
				System.out.println("No se ha podido insertar");

			}
		} catch (Exception e) {

		}
	}

	public void datosGrupo() {
		g1 = new Grupo();
		System.out.println("Introduzca el id del grupo");
		g1.setId(sc.nextInt());
		sc.nextLine();
		System.out.println("Introduzca el nombre del grupo");
		g1.setGrupo(sc.nextLine());

		gLista.add(g1);
		insertarGrupos(g1);

	}

	public void insertarGrupos(Grupo g1) {

		String sql = "INSERT INTO grupos (id_grupo, nombre) VALUES (?, ?)";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setInt(1, g1.getId());
			ps.setString(2, g1.getGrupo());

			int filas = ps.executeUpdate();
			if (filas > 0) {
				System.out.println("Grupo insertado");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void mostrarGrupos() {
		gLista.clear();

		String sql = "SELECT * FROM grupos";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();) {

			while (rs.next()) {
				g1 = new Grupo();

				g1.setId(rs.getInt("id_grupo"));
				g1.setGrupo(rs.getString("nombre"));
				gLista.add(g1);
			}
			for (Grupo grupo : gLista) {
				System.out.println(grupo);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void mostrarAlumnos() {
		String sql = "SELECT a.nia, a.nombre, a.apellido, g.nombre FROM alumnos a JOIN grupos g ON a.id_grupo = g.id_grupo";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();) {
			while (rs.next()) {
				System.out.printf("NIA: %d, Nombre: %s, Apellidos: %s, Nombre del Grupo: %s \n", rs.getInt("a.nia"),
						rs.getString("a.nombre"), rs.getString("a.apellido"), rs.getString("g.nombre"));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public ArrayList<Alumno> recogerAlumnos() {
	    aLista.clear(); 
	    String sql = "SELECT nia, nombre, apellido, genero, fechaNac, ciclo, curso, id_grupo FROM alumnos";

	    try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery();) {

	        while (rs.next()) {
	            a1 = new Alumno();

	            a1.setNia(rs.getInt("nia"));
	            a1.setNombre(rs.getString("nombre"));
	            a1.setApellidos(rs.getString("apellido"));

	            String genero = rs.getString("genero");
	            if (genero != null && !genero.isEmpty()) {
	                a1.setGenero(genero.charAt(0));
	            }

	            if (rs.getDate("fechaNac") != null) {
	                java.util.Date fecha = new java.util.Date(rs.getDate("fechaNac").getTime());
	                a1.setFechaNac(fecha);
	            }

	            a1.setCiclo(rs.getString("ciclo"));
	            a1.setCurso(rs.getString("curso"));
	            a1.setGrupo(String.valueOf(rs.getInt("id_grupo")));

	            aLista.add(a1);
	        }



	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }
	    return aLista;
	}

	public void ficheroBinario() {
		System.out.println(" Introducir ruta del archivo");

		String ruta = sc.nextLine();

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
			for (Alumno alumno : recogerAlumnos()) {
				oos.writeObject(alumno);
				System.out.println("insertando "+alumno);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void leerbinario() {
		aLista.clear();
		System.out.println("Introducir ruta del archivo");
		String ruta = sc.nextLine();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {

			while(true) {
				try {
					a1= new Alumno();
					a1=(Alumno) ois.readObject();
					insertarDesdeFichero(a1);
					aLista.add(a1);
				} catch (Exception e) {
					break;
				}
			}
			for (Alumno alumno : aLista) {
				System.out.println(alumno);
			}

		} catch (IOException e) {
			// TODO: handle exception
		}

	}
	
	public void insertarDesdeFichero(Alumno a1) {

		String sql = "INSERT INTO alumnos (nia, nombre, apellido, genero, fechaNac, ciclo, curso, id_grupo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setInt(1, a1.getNia());
			ps.setString(2, a1.getNombre());
			ps.setString(3, a1.getApellidos());
			ps.setString(4, String.valueOf(a1.getGenero()));

			java.sql.Date fechaSql = new java.sql.Date(a1.getFechaNac().getTime());
			ps.setDate(5, fechaSql);

			ps.setString(6, a1.getCiclo());
			ps.setString(7, a1.getCurso());
			ps.setInt(8, Integer.parseInt(a1.getGrupo()));

			int filas = ps.executeUpdate();
			if (filas > 0) {
				System.out.println("Alumno insertado");
			} else {
				System.out.println("No se ha podido insertar");

			}
		} catch (Exception e) {

		}
	}
	
	public void escribirTxt() {
	    ArrayList<Alumno> aux = recogerAlumnos(); // Obtener lista de alumnos desde la BD.
	    System.out.println("Introducir ruta del archivo");
	    String ruta = sc.nextLine();

	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
	        for (Alumno alumno : aux) {
	            // Escribir datos del alumno en el archivo.
	            bw.write(String.valueOf(alumno.getNia())); // NIA (siempre estará presente)
	            bw.newLine();

	            if (alumno.getNombre() != null) {
	                bw.write(alumno.getNombre()); // Nombre
	                bw.newLine();
	            }

	            if (alumno.getApellidos() != null) {
	                bw.write(alumno.getApellidos()); // Apellidos
	                bw.newLine();
	            }

	            if (alumno.getGenero() != 0) { // El género como char
	                bw.write(String.valueOf(alumno.getGenero()));
	                bw.newLine();
	            }

	            if (alumno.getFechaNac() != null) { // Formatear fecha si existe
	                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	                String fecha = format.format(alumno.getFechaNac());
	                bw.write(fecha);
	                bw.newLine();
	            }

	            if (alumno.getCiclo() != null) {
	                bw.write(alumno.getCiclo()); // Ciclo
	                bw.newLine();
	            }

	            if (alumno.getCurso() != null) {
	                bw.write(alumno.getCurso()); // Curso
	                bw.newLine();
	            }

	            if (alumno.getGrupo() != null) {
	                bw.write(alumno.getGrupo()); // Grupo
	                bw.newLine();
	            }

	            System.out.println(alumno + " insertado correctamente");
	        }

	        System.out.println("Archivo escrito correctamente en: " + ruta);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void modificarNombrePK() {
		System.out.println("Indique el nia del alumno");
		int nia=sc.nextInt();
		sc.nextLine();
		System.out.println("Indique el nuevo nombre");
		String nombre=sc.nextLine();
		
		String sql="UPDATE alumnos SET nombre = ? WHERE nia = ?";
		try(Connection conn=DriverManager.getConnection(URL,USER,PASS);
				PreparedStatement ps=conn.prepareStatement(sql);) {
			
			ps.setString(1, nombre);
			ps.setInt(2, nia);
			
			int filas=ps.executeUpdate();
			if(filas>0) {
				System.out.println("Alumno modificado");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void eliminarPK() {
		System.out.println("Indique el nia dela alumno que desee eliminar");
		int nia=sc.nextInt();
		sc.nextLine();
		String sql="DELETE FROM alumnos WHERE nia = ?";
		try (Connection conn=DriverManager.getConnection(URL,USER,PASS);
				PreparedStatement ps=conn.prepareStatement(sql);){
			ps.setInt(1, nia);
			
			int filas=ps.executeUpdate();
			if(filas>0) {
				System.out.println("Alumno eliminado");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void eliminarCurso() {
		System.out.println("Introduce el curso del alumno que desea eliminar");
		String sql1="SELECT curso FROM alumnos";
		try(Connection conn=DriverManager.getConnection(URL,USER,PASS);
				PreparedStatement ps=conn.prepareStatement(sql1);
				ResultSet rs=ps.executeQuery();) {
			
			while (rs.next()) {
				String curso=rs.getString("curso");
				System.out.println(curso);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		String cursoE=sc.nextLine();
		String sql="DELETE FROM alumnos WHERE curso LIKE ?";
		try (Connection conn=DriverManager.getConnection(URL,USER,PASS);
				PreparedStatement ps= conn.prepareStatement(sql);) {
			ps.setString(1, cursoE);
			
			int filas=ps.executeUpdate();
			if(filas>0) {
				System.out.println("Alumno eliminado");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void escribirXML() {
		try {
			DocumentBuilderFactory f= DocumentBuilderFactory.newDefaultInstance();
			DocumentBuilder b = f.newDocumentBuilder();
			Document d=b.newDocument();
			
			Element rootElement=d.createElement("Alumnos");
			d.appendChild(rootElement);
			
			for (Alumno alumno : recogerAlumnos()) {
				Element al=d.createElement("Alumno");
				
				Element nia=d.createElement("NIA");
				nia.appendChild(d.createTextNode(String.valueOf(alumno.getNia())));  
				al.appendChild(nia);
				
				Element nombre=d.createElement("Nombre");
				nombre.appendChild(d.createTextNode(alumno.getNombre()));
				al.appendChild(nombre);
				
				Element apellido=d.createElement("Apellidos");
				apellido.appendChild(d.createTextNode(alumno.getApellidos()));
				al.appendChild(apellido);
				
				Element genero=d.createElement("Genero");
				genero.appendChild(d.createTextNode(String.valueOf(alumno.getGenero())));
				al.appendChild(genero);
				
				if(alumno.getFechaNac()!=null) {
//					SimpleDateFormat formato= new SimpleDateFormat("dd/mm/yyyy");
//					String fechaM=formato.format(alumno.getFechaNac());
					
					Element fechaXML=d.createElement("FechaNacimiento");
//					fechaXML.appendChild(d.createTextNode(fechaM));
					fechaXML.appendChild(d.createTextNode(String.valueOf(a1.getFechaNac())));

					al.appendChild(fechaXML);
				}else {
					Element fecha=d.createElement("FechaNacimiento");
					fecha.appendChild(d.createTextNode(""));
					al.appendChild(fecha);
				}

				
				Element ciclo=d.createElement("Ciclo");
				ciclo.appendChild(d.createTextNode(alumno.getCiclo()));
				al.appendChild(ciclo);
				
				Element curso=d.createElement("Curso");
				curso.appendChild(d.createTextNode(alumno.getCurso()));
				al.appendChild(curso);
				
				Element grupo=d.createElement("Grupo");
				grupo.appendChild(d.createTextNode(alumno.getGrupo()));
				al.appendChild(grupo);
				
				rootElement.appendChild(al);


			}
			TransformerFactory tf= TransformerFactory.newInstance();
			Transformer t=tf.newTransformer();
			
		     // Configuración para formato legible
	        t.setOutputProperty(OutputKeys.INDENT, "yes");
	        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        
			DOMSource source = new DOMSource(d);
			StreamResult result =new StreamResult(new File("/Users/datos/Desktop/ficheros/alumnos.xml"));
			t.transform(source, result);
			System.out.println("XML generado");

			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void escribirAtributos() {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document documento = builder.newDocument();
			
			Element rootElement = documento.createElement("alumnos");
			documento.appendChild(rootElement);
			
	        for (Alumno alumno : recogerAlumnos()) {

	            Element alumnoElement = documento.createElement("alumno");
	            alumnoElement.setAttribute("nia", String.valueOf(alumno.getNia()));
	            alumnoElement.setAttribute("nombre", alumno.getNombre());
	            alumnoElement.setAttribute("apellidos", alumno.getApellidos());
	            alumnoElement.setAttribute("genero", String.valueOf(alumno.getGenero()));
	            alumnoElement.setAttribute("fechaNacimiento", String.valueOf(alumno.getFechaNac()));
	            alumnoElement.setAttribute("ciclo", alumno.getCiclo());
	            alumnoElement.setAttribute("curso", alumno.getCurso());
	            alumnoElement.setAttribute("grupo", alumno.getGrupo());

	            rootElement.appendChild(alumnoElement);
	        }
	        
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
		     // Configuración para formato legible
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        
			DOMSource source = new DOMSource(documento);
			StreamResult result = new StreamResult(new File("/Users/datos/Desktop/ficheros/alumnos2.xml"));
			transformer.transform(source, result);
			System.out.println("XML generado");
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void leerXml() {
	    try {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document documento = builder.parse(new File("/Users/datos/Desktop/ficheros/alumnos2.xml"));

	        NodeList listaAlumnos = documento.getElementsByTagName("alumno");

	        for (int i = 0; i < listaAlumnos.getLength(); i++) {
	            Node nodo = listaAlumnos.item(i);

	            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
	                Element alumnoElement = (Element) nodo;

	                // Crear un nuevo objeto Alumno
	                a1 = new Alumno();

	                // Usar setters para asignar los valores desde los atributos del elemento XML
	                a1.setNia(Integer.parseInt(alumnoElement.getAttribute("nia")));
	                a1.setNombre(alumnoElement.getAttribute("nombre"));
	                a1.setApellidos(alumnoElement.getAttribute("apellidos"));
	                a1.setGenero(alumnoElement.getAttribute("genero").charAt(0));

	                String fechaNacimientoStr = alumnoElement.getAttribute("fechaNacimiento");
	                if (!fechaNacimientoStr.isEmpty()) {
	                    SimpleDateFormat formato = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.ENGLISH);
	                    a1.setFechaNac(formato.parse(fechaNacimientoStr));
	                } else {
	                    a1.setFechaNac(null);
	                }

	                a1.setCiclo(alumnoElement.getAttribute("ciclo"));
	                a1.setCurso(alumnoElement.getAttribute("curso"));
	                a1.setGrupo(alumnoElement.getAttribute("grupo"));

	                insertarDesdeFichero(a1);
	                
	                System.out.println(a1+" Inserado");
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public void leerSinAtributos() {
	    try {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document documento = builder.parse(new File("/Users/datos/Desktop/ficheros/alumnos.xml"));

	        NodeList listaAlumnos = documento.getElementsByTagName("Alumno");

	        for (int i = 0; i < listaAlumnos.getLength(); i++) {
	            Node nodo = listaAlumnos.item(i);

	            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
	                Element alumnoElement = (Element) nodo;

	                a1 = new Alumno();

	                // Obtener los valores de los elementos hijos y usarlos con los setters
	                a1.setNia(Integer.parseInt(alumnoElement.getElementsByTagName("NIA").item(0).getTextContent()));
	                a1.setNombre(alumnoElement.getElementsByTagName("Nombre").item(0).getTextContent());
	                a1.setApellidos(alumnoElement.getElementsByTagName("Apellidos").item(0).getTextContent());
	                a1.setGenero(alumnoElement.getElementsByTagName("Genero").item(0).getTextContent().charAt(0));

	                String fechaNacimientoStr = alumnoElement.getElementsByTagName("FechaNacimiento").item(0).getTextContent();
	                if (!fechaNacimientoStr.isEmpty() && !fechaNacimientoStr.equals("null")) {
	                    SimpleDateFormat formato = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.ENGLISH);
	                    a1.setFechaNac(formato.parse(fechaNacimientoStr));
	                } else {
	                    a1.setFechaNac(null);
	                }

	                a1.setCiclo(alumnoElement.getElementsByTagName("Ciclo").item(0).getTextContent());
	                a1.setCurso(alumnoElement.getElementsByTagName("Curso").item(0).getTextContent());
	                a1.setGrupo(alumnoElement.getElementsByTagName("Grupo").item(0).getTextContent());
	                
	                insertarDesdeFichero(a1);
	                System.out.println(a1+ "Insertado");
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


}
