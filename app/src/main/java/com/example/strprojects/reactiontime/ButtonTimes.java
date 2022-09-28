package com.example.strprojects.reactiontime;

import java.util.Date;

public class ButtonTimes {
    private Date showDate;
    private Date hiddenDate;

    public ButtonTimes(){

    }

    public ButtonTimes(Date showDate, Date hiddenDate) {
        this.showDate = showDate;
        this.hiddenDate = hiddenDate;
    }

    public Date getShowDate() {
        return showDate;
    }

    public void setShowDate(Date showDate) {
        this.showDate = showDate;
    }

    public Date getHiddenDate() {
        return hiddenDate;
    }

    public void setHiddenDate(Date hiddenDate) {
        this.hiddenDate = hiddenDate;
    }
}
