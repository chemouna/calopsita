package br.com.caelum.calopsita.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;
import org.picocontainer.annotations.Inject;

import br.com.caelum.calopsita.repository.ProjectRepository;

@Entity
public class Project implements Identifiable {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;
    private String description;

    @ManyToOne
    private User owner;

    @ManyToMany
    private List<User> colaborators;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Iteration> iteractions;

    public Project(Long id) {
		this.id = id;
	}
    public Project() {
	}
    @Transient
    private ProjectRepository repository;

    @Inject
    public void setRepository(ProjectRepository repository) {
		this.repository = repository;
	}

    private ProjectRepository getRepository() {
    	if (repository == null) {
			throw new IllegalStateException("Repository was not set. You should inject it first");
		}
		return repository;
	}

    public Project load() {
    	return repository.load(this);
    }
    public Project refresh() {
    	return getRepository().refresh(this);
    }

    public void delete() {
    	getRepository().remove(this);
    }

    public List<User> getUnrelatedUsers() {
    	return getRepository().listUnrelatedUsers(this);
    }

    public void save() {
    	getRepository().add(this);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getColaborators() {
    	if (colaborators == null) {
			colaborators = new ArrayList<User>();
		}

        return colaborators;
    }

    public void setColaborators(List<User> colaborators) {
        this.colaborators = colaborators;
    }

    public List<Iteration> getIterations() {
        return iteractions;
    }

    public void setIteractions(List<Iteration> iteractions) {
        this.iteractions = iteractions;
    }
}
