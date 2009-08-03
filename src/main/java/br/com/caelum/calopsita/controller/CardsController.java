package br.com.caelum.calopsita.controller;

import static br.com.caelum.vraptor.view.Results.logic;
import static br.com.caelum.vraptor.view.Results.page;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isIn;

import java.util.List;

import br.com.caelum.calopsita.infra.vraptor.SessionUser;
import br.com.caelum.calopsita.model.Card;
import br.com.caelum.calopsita.model.Gadgets;
import br.com.caelum.calopsita.model.Project;
import br.com.caelum.calopsita.model.User;
import br.com.caelum.calopsita.repository.CardRepository;
import br.com.caelum.calopsita.repository.ProjectRepository;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Hibernate;
import br.com.caelum.vraptor.validator.Validations;

@Resource
public class CardsController {

	private static final String UPDATE_JSP = "/WEB-INF/jsp/cards/update.jsp";
	private final CardRepository repository;
	private final User currentUser;
	private final ProjectRepository projectRepository;
    private final Validator validator;
    private final Result result;

	public CardsController(Result result, Validator validator, SessionUser user, CardRepository repository, ProjectRepository projectRepository) {
		this.result = result;
        this.validator = validator;
        this.currentUser = user == null? null: user.getUser();
		this.repository = repository;
		this.projectRepository = projectRepository;
	}

	@Path("/projects/{project.id}/cards/") @Get
    public void list(Project project) {
    	this.result.include("project", project.load());
    	this.result.include("cards",  project.getCards());
    	this.result.include("gadgets", Gadgets.values());
    	this.result.include("cardTypes", project.getCardTypes());
    }

	@Path("/projects/{card.project.id}/cards/") @Post
	public void save(final Card card, List<Gadgets> gadgets) {
		validator.checking(new Validations() {
            {
                that(Hibernate.validate(card));
            }
        });
		card.save();
		if (gadgets != null) {
			for (Gadgets gadget : gadgets) {
				repository.add(gadget.createGadgetFor(card));
			}
		}
		result.include("project", card.getProject());
		result.include("cards", card.getProject().getCards());
		result.use(page()).forward(UPDATE_JSP);
	}

	@Path("/projects/{card.project.id}/cards/{card.parent.id}/subcards/") @Post
	public void saveSub(Card card) {
		card.save();
		result.include("cards", card.getParent().getSubcards());
		result.include("card", card.getParent());
		result.include("project", card.getProject());
		result.use(page()).forward(UPDATE_JSP);
	}

	@Path("/projects/{card.project.id}/cards/{card.id}/") @Get
	public void edit(Card card) {
	    card.refresh();
		result.include("card", card);
		result.include("project", card.getProject());
		result.include("gadgets", Gadgets.values());
	    result.include("cardGadgets", Gadgets.valueOf(card.getGadgets()));
	    result.include("cards", card.getSubcards());
	    result.include("cardTypes", card.getProject().getCardTypes());
	}

	@Path("/projects/{card.project.id}/cards/{card.id}/") @Post
	public void update(Card card, List<Gadgets> gadgets) {
		Card loaded = card.load();

		loaded.setName(card.getName());
		loaded.setDescription(card.getDescription());

		Project project = loaded.getProject();
		loaded.updateGadgets(gadgets);
		result.use(logic()).redirectTo(CardsController.class).list(project);
	}

	@Path("/projects/{card.project.id}/cards/{card.id}/") @Delete
	public void delete(Card card, boolean deleteSubcards) {
		Card loaded = repository.load(card);
		final Project project = loaded.getProject();

		validator.checking(new Validations() {
			{
				that(currentUser, anyOf(
							isIn(project.getColaborators()),
							is(equalTo(project.getOwner()))));
			}
		});
        if (deleteSubcards) {
            for (Card sub : loaded.getSubcards()) {
                repository.remove(sub);
            }
        } else {
            for (Card sub : loaded.getSubcards()) {
                sub.setParent(null);
                repository.update(sub);
            }
        }
        repository.remove(loaded);
        result.include("cards", this.projectRepository.listCardsFrom(project));
        result.include("project", project);
        result.use(page()).forward(UPDATE_JSP);
	}

}
