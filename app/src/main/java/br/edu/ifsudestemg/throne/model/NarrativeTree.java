package br.edu.ifsudestemg.throne.model;

public class NarrativeTree {

    private NarrativeCard root;

    private NarrativeCard yesBranch;

    private NarrativeCard noBranch;

    private NarrativeCard yesYesLeaf;

    private NarrativeCard yesNoLeaf;

    private NarrativeCard noYesLeaf;

    private NarrativeCard noNoLeaf;

    private TwistNarrative twist;

    public NarrativeTree() {}

    public NarrativeCard getRoot() {
        return root;
    }

    public NarrativeCard getYesBranch() {
        return yesBranch;
    }

    public NarrativeCard getNoBranch() {
        return noBranch;
    }

    public NarrativeCard getYesYesLeaf() {
        return yesYesLeaf;
    }

    public NarrativeCard getYesNoLeaf() {
        return yesNoLeaf;
    }

    public NarrativeCard getNoYesLeaf() {
        return noYesLeaf;
    }

    public NarrativeCard getNoNoLeaf() {
        return noNoLeaf;
    }

    public TwistNarrative getTwist() {
        return twist;
    }

    public void setRoot(NarrativeCard root) {
        this.root = root;
    }

    public void setYesBranch(NarrativeCard yesBranch) {
        this.yesBranch = yesBranch;
    }

    public void setNoBranch(NarrativeCard noBranch) {
        this.noBranch = noBranch;
    }

    public void setYesYesLeaf(NarrativeCard yesYesLeaf) {
        this.yesYesLeaf = yesYesLeaf;
    }

    public void setYesNoLeaf(NarrativeCard yesNoLeaf) {
        this.yesNoLeaf = yesNoLeaf;
    }

    public void setNoYesLeaf(NarrativeCard noYesLeaf) {
        this.noYesLeaf = noYesLeaf;
    }

    public void setNoNoLeaf(NarrativeCard noNoLeaf) {
        this.noNoLeaf = noNoLeaf;
    }

    public void setTwist(TwistNarrative twist) {
        this.twist = twist;
    }
}