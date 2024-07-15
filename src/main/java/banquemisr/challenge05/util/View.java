package banquemisr.challenge05.util;


public class View {


   public interface Page {

   }
    public interface BasicTask {

    }

    public interface BasicUser {

    }

    public interface BasicAuthority {

    }


    public interface UserAuthority {

    }

    public interface UserPage extends Page ,BasicUser{

    }

    public interface TaskPage extends Page , BasicTask ,BasicUser{
    }
}
