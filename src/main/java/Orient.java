public enum Orient {
    oriented(true),
    notOriented(false);
    private boolean value;

    Orient(Boolean value) {
        this.value = value;
    }

    public Boolean getOrient() {
        return this.value;
    }

}
