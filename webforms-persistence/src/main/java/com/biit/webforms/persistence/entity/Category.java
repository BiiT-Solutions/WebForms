package com.biit.webforms.persistence.entity;

import com.biit.form.entity.BaseCategory;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.serialization.CategoryDeserializer;
import com.biit.webforms.serialization.CategorySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@JsonDeserialize(using = CategoryDeserializer.class)
@JsonSerialize(using = CategorySerializer.class)
@Table(name = "tree_categories")
@Cacheable()
public class Category extends BaseCategory implements ElementWithMedia {
	private static final long serialVersionUID = 7418748035993485582L;
	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDREN = new ArrayList<>(Arrays.asList(BaseQuestion.class,
			BaseRepeatableGroup.class));

	@OneToOne(mappedBy = "element", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private TreeObjectImage image;

	@OneToOne(mappedBy = "element", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private TreeObjectVideo video;

	@OneToOne(mappedBy = "element", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private TreeObjectAudio audio;

	public Category() {
		super();
	}

	public Category(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (image != null) {
			image.resetIds();
		}
	}

	@Override
	protected void resetDatabaseIds() {
		super.resetDatabaseIds();
		if (image != null) {
			image.resetDatabaseIds();
		}
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDREN;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof Category) {
			// Nothing to copy except basic information data.
			copyBasicInfo(object);
		} else {
			throw new NotValidTreeObjectException("Copy data for Category only supports the same type copy");
		}
	}

	public int exportToJavaCode(StringBuilder sb, int counter) {
		String idName = "el_" + counter;

		sb.append("Category ").append(idName).append("  = new Category();").append(System.lineSeparator());
		sb.append(idName).append(".setName(\"").append(this.getName()).append("\");").append(System.lineSeparator());
		sb.append(idName).append(".setLabel(\"").append(this.getLabel()).append("\");").append(System.lineSeparator());

		int currentCounter = counter;
		for (TreeObject child : getChildren()) {
			int tempCounter = currentCounter + 1;
			if (child instanceof Group) {
				currentCounter = ((Group) child).exportToJavaCode(sb, currentCounter + 1);
			}
			if (child instanceof WebformsBaseQuestion) {
				currentCounter = ((WebformsBaseQuestion) child).exportToJavaCode(sb, currentCounter + 1);
			}

			sb.append("//cat").append(System.lineSeparator());
			sb.append(idName).append(".addChild(").append("el_" + tempCounter).append(");").append(System.lineSeparator());
		}
		return currentCounter;
	}

	/**
	 * This function checks if the content of two category elements is the same.
	 *
	 * @return
	 */

	@Override
	public boolean isContentEqual(TreeObject treeObject) {
		if (treeObject instanceof Category) {
			return super.isContentEqual(treeObject);
		}
		return false;
	}

	@Override
	public void setImage(TreeObjectImage image) {
		this.image = image;
		if (image != null) {
			image.setElement(this);
		}
	}

	@Override
	public TreeObjectImage getImage() {
		return image;
	}


	@Override
	public void setVideo(TreeObjectVideo video) {
		this.video = video;
		if (video != null) {
			video.setElement(this);
		}
	}

	@Override
	public TreeObjectVideo getVideo() {
		return video;
	}

	@Override
	public void setAudio(TreeObjectAudio audio) {
		this.audio = audio;
		if (audio != null) {
			audio.setElement(this);
		}
	}

	@Override
	public TreeObjectAudio getAudio() {
		return audio;
	}
}
