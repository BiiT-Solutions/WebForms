package com.biit.webforms.persistence.entity;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.util.Base64Utils;

import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.exceptions.InvalidRemoteImageDefinition;

@Entity
@Table(name = "images")
@Cacheable(true)
public class TreeObjectImage extends StorableObject {
	public static final int MAX_IMAGE_LENGTH = 1024 * 1024 * 10;
	private static final long serialVersionUID = 1072375747626406485L;

	@Column
	private String fileName;

	private int width;

	private int height;

	@Lob
	@Column(length = MAX_IMAGE_LENGTH)
	private byte[] data;

	@OneToOne(optional = false)
	private TreeObject element;

	@Column(columnDefinition = "TEXT")
	private String url;

	public TreeObjectImage() {

	}

	protected void resetDatabaseIds() {
		setId(null);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public byte[] getData() {
		return data;
	}

	public byte[] getDataFromUrl() throws InvalidRemoteImageDefinition {
		try {
			URL urlPath = new URL(getUrl());
			return getDataFromUrl(urlPath);
		} catch (IOException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			throw new InvalidRemoteImageDefinition(e.getMessage());
		}
	}

	private byte[] getDataFromUrl(URL url) throws IOException {
		if (url == null) {
			return null;
		}
		BufferedImage bufferedImage = ImageIO.read(url);

		// get DataBufferBytes from Raster
		WritableRaster raster = bufferedImage.getRaster();
		DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

		return data.getData();
	}

	public void setData(byte[] data) {
		this.data = data;
		url = null;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		return new HashSet<>();
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TreeObjectImage) {
			copyBasicInfo(object);
			this.setWidth(((TreeObjectImage) object).getWidth());
			this.setHeight(((TreeObjectImage) object).getHeight());
			setData(Arrays.copyOf(((TreeObjectImage) object).getData(), ((TreeObjectImage) object).getData().length));
		} else {
			throw new NotValidStorableObjectException("Copy data for Images only supports the same type copy");
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ByteArrayOutputStream getStream() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(getData().length);
		baos.write(getData(), 0, getData().length);
		return baos;
	}

	public void setStream(ByteArrayOutputStream baos) {
		setData(baos.toByteArray());
	}

	@Override
	public String toString() {
		return getFileName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = Arrays.hashCode(data);
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + height;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		TreeObjectImage other = (TreeObjectImage) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	/**
	 * Gets the image in base64
	 * 
	 * @return
	 */
	public String toBase64() {
		return Base64Utils.encodeToString(getData());
	}

	/**
	 * sets the image from base64
	 */
	public void fromBase64(String data) {
		setData(Base64Utils.decodeFromString(data));
	}

	public void setDefaultHeightAndWeight() {
		InputStream in = new ByteArrayInputStream(getData());
		try {
			BufferedImage bimg = ImageIO.read(in);
			if (bimg != null) {
				// Disable field to disable events to be launched.
				setWidth(bimg.getWidth());
				setHeight(bimg.getHeight());
			}
		} catch (IOException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public TreeObject getElement() {
		return element;
	}

	public void setElement(TreeObject element) {
		this.element = element;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		data = null;
	}

}
