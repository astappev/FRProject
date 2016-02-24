package astappev.extractPersons;

/**
 * This class represents entities and their position in some text.
 *
 */
public class PosEntity {
	private StringBuilder sb = new StringBuilder();
	private int beginpos = Integer.MAX_VALUE;
	private int endpos = -1;
	private int length = 0;
	private int startWord = -1;
	private int endWord;
	private int sentence;
	private int personId;

	/**
	 * This class represents entities and their position in some text.
	 */
	public PosEntity() {
		// clear();
	}

	/**
	 * New PosEntity with the given parameters set.
	 * 
	 * @param aToken
	 *            The name of the person
	 * @param wordCounter
	 *            Position of the name
	 * @param sentenceCounter
	 *            Sentence of the name
	 */
	public PosEntity(String aToken, int wordCounter, int sentenceCounter) {
		// clear();
		addLable(aToken, wordCounter, sentenceCounter);
	}

	/*
	 * public void clear() { beginpos = Integer.MAX_VALUE; endpos = -1; length =
	 * 0; startWord = -1; sb.setLength(0); }
	 */

	private void addLable(String aToken, int wordCounter, int sentenceCounter) {
		length++;
		/*
		 * if(beginpos > aToken.beginPosition()) beginpos =
		 * aToken.beginPosition(); if(endpos < aToken.endPosition()) endpos =
		 * aToken.endPosition();
		 * 
		 */
		if (sb.length() > 0)
			sb.append(" ");
		sb.append(aToken);

		if (startWord < 0)
			startWord = wordCounter;

		endWord = wordCounter;
		sentence = sentenceCounter;
		length = aToken.split(" ").length;
	}

	/**
	 * Adds an entity.
	 * 
	 * @param entity
	 */
	public void addEntity(PosEntity entity) {
		length += entity.getLength();

		if (beginpos > entity.getBeginpos())
			beginpos = entity.getBeginpos();
		if (endpos < entity.getEndpos())
			endpos = entity.getEndpos();

		if (sb.length() > 0)
			sb.append(" ");
		sb.append(entity.getLable());

		if (startWord < 0)
			startWord = entity.getStartWord();

		endWord = entity.getEndWord();
	}

	/**
	 * Getter for the label.
	 * 
	 * @return
	 */
	public String getLable() {
		return sb.toString();
	}

	/**
	 * Getter for Beginpos
	 * 
	 * @return
	 */
	public int getBeginpos() {
		return beginpos;
	}

	/**
	 * Getter for Endpos.
	 * 
	 * @return
	 */
	public int getEndpos() {
		return endpos;
	}

	/**
	 * Getter for getLength.
	 * 
	 * @return
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Getter for startWord.
	 * 
	 * @return
	 */
	public int getStartWord() {
		return startWord;
	}

	/**
	 * Getter for EndWord
	 * 
	 * @return
	 */
	public int getEndWord() {
		return endWord;
	}

	/**
	 * Getter for sentence.
	 * 
	 * @return
	 */
	public int getSentence() {
		return sentence;
	}

	/**
	 * Getter for the ID of the person.
	 * 
	 * @return
	 */
	public int getPersonId() {
		return personId;
	}

	/**
	 * Setter for the person ID.
	 * 
	 * @param personId
	 */
	public void setPersonId(int personId) {
		this.personId = personId;
	}

	/**
	 * Returns a clone of this PosEntity.
	 */
	@Override
	public PosEntity clone() {
		PosEntity clone = new PosEntity();
		clone.beginpos = beginpos;
		clone.endpos = endpos;
		clone.length = length;
		clone.startWord = startWord;
		clone.endWord = endWord;
		clone.sentence = sentence;
		clone.personId = personId;
		clone.sb = new StringBuilder(sb.toString());

		return clone;
	}

	/**
	 * Returns a String containing all the fields.
	 */
	@Override
	public String toString() {
		return "PosEntity [lable=" + sb + ", beginpos=" + beginpos + ", endpos=" + endpos + ", length=" + length
				+ ", startWord=" + startWord + ", endWord=" + endWord + ", sentence=" + sentence + ", personId="
				+ personId + "]";
	}

	/**
	 * Setter for beginPos.
	 * 
	 * @param beginpos
	 */
	public void setBeginpos(int beginpos) {
		this.beginpos = beginpos;
	}

	/**
	 * Setter for endPos.
	 * 
	 * @param endPos
	 */
	public void setEndpos(int endpos) {
		this.endpos = endpos;
	}

	/**
	 * Setter for startWord.
	 * 
	 * @param startWord
	 */
	public void setStartWord(int startWord) {
		this.startWord = startWord;
	}

	/**
	 * Setter for endWord.
	 * 
	 * @param endWord
	 */
	public void setEndWord(int endWord) {
		this.endWord = endWord;
	}

	/**
	 * Setter for sentence.
	 * 
	 * @param sentence
	 */
	public void setSentence(int sentence) {
		this.sentence = sentence;
	}

}
