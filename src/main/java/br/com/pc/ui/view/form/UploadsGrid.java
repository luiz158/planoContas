package br.com.pc.ui.view.form;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;


public class UploadsGrid implements  Upload.SucceededListener,
									Upload.FailedListener,
									Upload.Receiver, InterfaceForms<String> {

	private static final long serialVersionUID = 1L;
	private Panel root;         // Root element for contained components.
//	Panel imagePanel;   // Panel that contains the uploaded image.
	private File  file;         // File to write to.
	private Label aviso;
	private String pasta;
	private String subPasta;
	private String registro;
	
	private VerticalLayout vLinks = new VerticalLayout();
//	private Link l = new Link();
//	private Table tabela;
	final Upload upload;
	
//	@Inject	private ResourceBundle bundle;
	
	public UploadsGrid(String subPasta) {
		root = new Panel();
		aviso = new Label("");
		geraRegistro();
		this.subPasta=subPasta;
//		pasta = bundle.getString("upload")+ "/" +
//				subPasta + "/";
		pasta = "../upload"+ "/" + subPasta + "/";

		upload = new Upload("Envie o arquivo", this);
		upload.setButtonCaption("Enviar");
		upload.addListener((Upload.SucceededListener) this);
		upload.addListener((Upload.FailedListener) this);

		root.addComponent(upload);
		root.addComponent(aviso);
		root.addComponent(vLinks);
		
		geraRegistro();

	}
	
	public OutputStream receiveUpload(String filename, String MIMEType) {
				FileOutputStream fos = null; // Output stream to write to
				file = new File(pasta +registro+"/"+filename);
				try {
				// Open the file for writing.
					File f2 = new File(pasta +registro+"/");
					if (!f2.isDirectory()){
						f2.mkdirs();
					}
					fos = new FileOutputStream(file);
				} catch (final java.io.FileNotFoundException e) {
					// Error while opening the file. Not reported here.
					e.printStackTrace();
					return null;
				}
			
				return fos; // Return the output stream to write to
		}
	
	// This is called if the upload is finished.
	public void uploadSucceeded(Upload.SucceededEvent event) {

		final FileResource fileResource = new FileResource(file, root.getApplication());
		aviso.setValue("Upload de " + event.getFilename() + " ok!");
		vLinks.addComponent(new Link(event.getFilename(),fileResource));
	}
	
	// This is called if the upload fails.
	public void uploadFailed(Upload.FailedEvent event) {
		aviso.setValue("Upload de " + event.getFilename() + " falhou!");
	}
	
	public void geraRegistro(){
		vLinks.removeAllComponents();
		registro = UUID.randomUUID().toString();
	}
	
	public String getSubPasta(){
		return subPasta;
	}
	
	public void setSubPasta(String subPasta){
		this.subPasta = subPasta;
	}
	
	public String getRegistro(){
		return registro;
	}
	
	public void setRegistro(String registro){
		this.registro = registro;
		listaArquivos();
	}
	
	public void listaArquivos(){
		vLinks.removeAllComponents();
		File dir = new File(pasta + registro);
		if (dir.listFiles().length>0){
			for (File file : dir.listFiles()) {
				final FileResource fileResource = new FileResource(file, root.getApplication());
				vLinks.addComponent(new Link(file.getName(),fileResource));
			}
		}
	}
	public Panel getPainel(){
		return root;
	}
	
	public String novo(){
		geraRegistro();
		return getRegistro();
	}
//	public void set(String registro){
//		setRegistro(registro);
//	}
//	public String get(){
//		return getRegistro();
//	}
	public void limpar(){
		
	}
	public void excluir(){
		
	}
	
	@Deprecated
	public void salvar(){
		
	}

	public void setBean(String bean) {
		setRegistro(bean);
	}

	public String getBean() {
		return getRegistro();
	}
}
