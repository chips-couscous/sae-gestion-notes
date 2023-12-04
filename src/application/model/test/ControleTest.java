package application.model.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import application.model.Controle;
import application.model.Note;
import application.model.exception.ControleInvalideException;
import application.model.exception.NoteInvalideException;

class ControleTest {

	@Test
	void testControleValide() throws ControleInvalideException {
		Controle controleValide = new Controle("écrit", "12/10/2023", 25);
		assertEquals("écrit", controleValide.getTypeControle());
		assertEquals("12/10/2023", controleValide.getDateControle());
		assertEquals(25, controleValide.getPoidsControle());
	}

	@Test
	void testControleSansType() {
		assertThrows(ControleInvalideException.class, () -> {
			new Controle("", "15/11/2023", 30);
		});
	}

	@Test
	void testControleSansDate() {
		assertThrows(ControleInvalideException.class, () -> {
			new Controle("oral", "", 20);
		});
	}

	@Test
	void testControlePoidsNegatif() {
		assertThrows(ControleInvalideException.class, () -> {
			new Controle("écrit", "20/11/2023", -5);
		});
	}

	@Test
	void testControleNoteValide() throws ControleInvalideException, NoteInvalideException {
		Note note = new Note(75, 100, "Très bien !");
		Controle controleAvecNote = new Controle("oral", "15/11/2023", 15);
		controleAvecNote.setNoteControle(note);
		assertEquals(note, controleAvecNote.getNoteControle());
	}
}