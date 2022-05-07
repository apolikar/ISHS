package irl.lyit.DublinSmartHouseSearch.presentation.homePanel.Exception;

public class FormValidationError extends Exception {
    private final String errorDescription;

    public FormValidationError(String errorTitle, String errorDescription) {
        super(errorTitle);

        this.errorDescription = errorDescription;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
