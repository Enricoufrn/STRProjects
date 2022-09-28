package com.example.strprojects.reactiontime;

import java.util.Date;

public class ButtonTimes {
    private Date showDate;
    private Date hiddenDate;
    private Date clickDate;

    public ButtonTimes(){

    }

    public ButtonTimes(Date showDate, Date hiddenDate, Date clickDate) {
        this.showDate = showDate;
        this.hiddenDate = hiddenDate;
        this.clickDate = clickDate;
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

    public Date getClickDate() {
        return clickDate;
    }

    public void setClickDate(Date clickDate) {
        this.clickDate = clickDate;
    }
}
