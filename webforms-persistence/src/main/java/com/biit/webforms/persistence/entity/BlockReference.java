package com.biit.webforms.persistence.entity;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.persistence.utils.IdGenerator;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.serialization.BlockReferenceDeserializer;
import com.biit.webforms.serialization.BlockReferenceSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonDeserialize(using = BlockReferenceDeserializer.class)
@JsonSerialize(using = BlockReferenceSerializer.class)
@Table(name = "tree_blocks_references")
@Cacheable()
public class BlockReference extends TreeObject implements IWebformsBlockView {
    private static final long serialVersionUID = -4300039254232003868L;

    public static final String DEFAULT_TECHNICAL_NAME = "block_reference";
    private static final List<Class<? extends TreeObject>> ALLOWED_CHILDREN = Collections.singletonList(Category.class);

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "reference")
    private Block reference;

    //Used mainly for json deserialize.
    private transient Long blockReferencedId;

    public BlockReference() {
        super();
    }

    public BlockReference(Block reference) {
        super();
        setReference(reference);
    }

    public Block getReference() {
        return reference;
    }

    public void setReference(Block reference) {
        this.reference = reference;
        if (reference != null) {
            this.blockReferencedId = reference.getId();
        }
    }

    @Override
    public void setStatus(FormWorkStatus status) {
        if (reference != null) {
            reference.setStatus(status);
        }
    }

    @Override
    public FormWorkStatus getStatus() {
        if (reference != null) {
            return reference.getStatus();
        }
        return null;
    }

    @Override
    public Set<Integer> getLinkedFormVersions() {
        return null;
    }

    @Override
    public Long getLinkedFormOrganizationId() {
        return null;
    }

    @Override
    public String getLinkedFormLabel() {
        return null;
    }

    public Long getBlockReferencedId() {
        return blockReferencedId;
    }

    public void setBlockReferencedId(Long blockReferencedId) {
        this.blockReferencedId = blockReferencedId;
    }

    @Override
    public Long getOrganizationId() {
        if (reference != null) {
            return reference.getOrganizationId();
        }
        return null;
    }

    @Override
    public Integer getVersion() {
        if (reference != null) {
            return reference.getVersion();
        }
        return null;
    }

    @Override
    public void resetIds() {
        setId(null);
        setComparationId(IdGenerator.createId());
    }

    @Override
    public boolean isLastVersion() {
        if (reference != null) {
            return reference.isLastVersion();
        }
        return true;
    }

    @Override
    public void checkDependencies() throws DependencyExistException {
        if (reference != null) {
            reference.checkDependencies();
        }
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof BlockReference) {
            // Nothing to copy except basic information data.
            copyBasicInfo(object);
            setReference(((BlockReference) object).getReference());
        } else {
            throw new NotValidTreeObjectException("Copy data for a Block Reference only supports the same type copy");
        }
    }

    @Override
    protected List<Class<? extends TreeObject>> getAllowedChildren() {
        return ALLOWED_CHILDREN;
    }

    @Override
    protected String getDefaultTechnicalName() {
        return DEFAULT_TECHNICAL_NAME;
    }

    @Override
    public String toString() {
        if (reference != null) {
            return "*" + reference.getLabel() + "*";
        }
        return "[empty]";
    }

    @Override
    public String getLabel() {
        if (reference != null) {
            return reference.getLabel();
        }
        return null;
    }

    @Override
    public String getName() {
        // Returns the name of the first category of the block
        try {
            return reference.getChild(0).getName();
        } catch (IndexOutOfBoundsException | ChildrenNotFoundException e) {
            return "";
        }
    }

    @Override
    public List<TreeObject> getChildren() {
        if (reference == null) {
            return new ArrayList<>();
        }
        return reference.getChildren();
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        if (reference != null) {
            return reference.getAllInnerStorableObjects();
        }
        return new HashSet<>();
    }

    /**
     * For some cases, i.e. using Springcache we need to initialize all sets
     * (disabling the Lazy loading).
     */
    @Override
    public void initializeSets() {
        super.initializeSets();
        reference.initializeSets();
    }

    @Override
    public Set<TreeObjectImage> getAllImages() {
        Set<TreeObjectImage> images = new HashSet<>();
        for (StorableObject children : getAllInnerStorableObjects()) {
            if (children instanceof ElementWithMedia) {
                if (((ElementWithMedia) children).getImage() != null) {
                    images.add(((ElementWithMedia) children).getImage());
                }
            }
        }
        return images;
    }

    @Override
    public boolean hasJson() {
        return false;
    }

    @Override
    public void setHasJson(boolean hasJson) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDroolsXPathName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TreeObject getChild(String pathstring) {
        if (reference == null) {
            return null;
        }
        // Skip categories in path, path level already consumed by block.
        for (TreeObject children : reference.getChildren()) {
            TreeObject child = children.getChild(pathstring);
            if (child != null) {
                return child;
            }
        }
        return null;
    }

    @Override
    public TreeObject getChild(List<String> childPath) {
        if (reference == null) {
            return null;
        }
        // Skip categories in path, path level already consumed by block.
        for (TreeObject children : reference.getChildren()) {
            TreeObject child = children.getChild(childPath);
            if (child != null) {
                return child;
            }
        }
        return null;
    }

    @Override
    public TreeObject getChild(String... pathStrings) {
        if (reference == null) {
            return null;
        }
        // Skip categories in path, path level already consumed by block.
        for (TreeObject children : reference.getChildren()) {
            TreeObject child = children.getChild(pathStrings);
            if (child != null) {
                return child;
            }
        }
        return null;
    }

    @Override
    public void print(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("\t");
        }
        System.out.println(this + " -- " + this.getClass().getName());
        reference.print(indent);
    }
}
