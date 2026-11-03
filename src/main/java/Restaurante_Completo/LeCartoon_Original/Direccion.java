package Restaurante_Completo.LeCartoon_Original;

public class Direccion {
	private String pais;
	private String estado;
	private String ciudad;
	private String municipio;
	private String cp; //Código Postal
	private String colonia;
	private String calle;
	private String numexterior;
	private String numinterior;
	
	public Direccion(String pais, String estado, String ciudad, String municipio, String cp, String colonia,
			String calle, String numexterior, String numinterior) {		
		
		this.pais = pais;
		this.estado = estado;
		this.ciudad = ciudad;
		this.municipio = municipio;
		this.cp = cp;
		this.colonia = colonia;
		this.calle = calle;
		this.numexterior = numexterior;
		this.numinterior = numinterior;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumexterior() {
		return numexterior;
	}

	public void setNumexterior(String numexterior) {
		this.numexterior = numexterior;
	}

	public String getNuminterior() {
		return numinterior;
	}

	public void setNuminterior(String numinterior) {
		this.numinterior = numinterior;
	}

}
