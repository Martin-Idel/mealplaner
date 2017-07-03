package mealplaner.model.settings;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import mealplaner.model.Meal;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.ObligatoryUtensil;

public class CookingUtensilSetting implements Serializable, CookingSetting {

	private static final long serialVersionUID = 1L;
	private final Set<ObligatoryUtensil> prohibitedUtensil;

	public CookingUtensilSetting() {
		this(new HashSet<ObligatoryUtensil>());
	}

	public CookingUtensilSetting(CookingUtensilSetting prohibitedUtensil) {
		this(new HashSet<ObligatoryUtensil>(prohibitedUtensil.prohibitedUtensil));
	}

	public CookingUtensilSetting(Set<ObligatoryUtensil> prohibitedUtensil) {
		this.prohibitedUtensil = prohibitedUtensil;
	}

	public void setCasseroleSettings(CasseroleSettings casserole) {
		if (casserole == CasseroleSettings.NONE) {
			prohibitedUtensil.add(ObligatoryUtensil.CASSEROLE);
		} else if (casserole == CasseroleSettings.ONLY) {
			prohibitedUtensil.addAll(Arrays.asList(ObligatoryUtensil.values()));
			prohibitedUtensil.remove(ObligatoryUtensil.CASSEROLE);
		}
	}

	public void setManyPeople() {
		prohibitedUtensil.add(ObligatoryUtensil.PAN);
	}

	public void reset() {
		prohibitedUtensil.removeAll(Arrays.asList(ObligatoryUtensil.values()));
	}

	public boolean isUtensilProhibited(ObligatoryUtensil prohibited) {
		return prohibitedUtensil.contains(prohibited);
	}

	@Override
	public boolean prohibits(Meal meal) {
		return prohibitedUtensil.contains(meal.getObligatoryUtensil());
	}

	public boolean contains(ObligatoryUtensil obligatoryUtensil) {
		return prohibitedUtensil.contains(obligatoryUtensil);
	}

	public boolean containsMany() {
		return (prohibitedUtensil.contains(ObligatoryUtensil.PAN)) ? true : false;
	}
}
