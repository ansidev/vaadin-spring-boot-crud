package xyz.ansidev.simple_crud.util.formatter;

public interface IFormatter<Model, Presentation> {
	public Presentation format(Model model);

	public Model parse(Presentation presentation);
}
