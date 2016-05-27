package xyz.ansidev.simple_crud.util.transformer;

import xyz.ansidev.simple_crud.entity.User;
import xyz.ansidev.simple_crud.ui.model.UserModel;

public class UserTransformer implements IModelTransformer<User, UserModel> {

	@Override
	public UserModel transformToPresentation(User user) {
		return new UserModel(user);
	}

	@Override
	public User transformToModel(UserModel userModel) {
		return new User(userModel);
	}

}
