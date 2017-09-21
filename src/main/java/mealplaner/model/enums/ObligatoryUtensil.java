package mealplaner.model.enums;

import java.util.EnumMap;

import mealplaner.BundleStore;

public enum ObligatoryUtensil {
	POT, PAN, CASSEROLE;

	public static EnumMap<ObligatoryUtensil, String> getObligatoryUtensilStrings(
			BundleStore bundles) {
		EnumMap<ObligatoryUtensil, String> obligatoryUtensilNames = new EnumMap<ObligatoryUtensil, String>(
				ObligatoryUtensil.class);
		obligatoryUtensilNames.put(ObligatoryUtensil.POT, bundles.message("pot"));
		obligatoryUtensilNames.put(ObligatoryUtensil.PAN, bundles.message("pan"));
		obligatoryUtensilNames.put(ObligatoryUtensil.CASSEROLE, bundles.message("casserole"));
		return obligatoryUtensilNames;
	}
}
