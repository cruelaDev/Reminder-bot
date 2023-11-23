package uzreminder.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRepository {
    private final static UserRepository repository = new UserRepository();
    private final List<User> users = new ArrayList<>();
    public Optional<User> findById(String id){
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
    public void save(String id){
        User user = new User(id,UserState.MAIN,new ArrayList<>());
        Optional<User> byId = findById(id);
        users.add(user);
    }

    public static UserRepository getInstance() {
        return repository;
    }
}
