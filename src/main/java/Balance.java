public enum Balance {
    weighted (true),
    notWeighted(false);

    private boolean value;

    Balance(Boolean value) {
        this.value = value;
    }

    public Boolean getBalance() {
        return this.value;
    }

}
