package xyz.ansidev.simple_crud.util.transformer;

import java.io.Serializable;

public interface IModelTransformer<Model extends Serializable, Presentation extends Serializable> {

	public Presentation transformToPresentation(Model entity);

	public Model transformToModel(Presentation model);
}
