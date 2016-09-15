package nilay.tipme;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nneeranjun on 8/28/16.
 */
public class User implements Parcelable {
    public String firstName;
    public String lastName;
    public String fullName;
    public String emailAddress;
    byte[] profilePicture;

    protected User(String firstName, String lastName, String emailAddress){
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        fullName = firstName+" "+lastName;
    }
    private User(Parcel in){
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.emailAddress = in.readString();
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getFullName(){
        return fullName;
    }
    public String getEmailAddress(){
        return emailAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
       parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(emailAddress);

    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private void setProfilePicture(){

    }
}
