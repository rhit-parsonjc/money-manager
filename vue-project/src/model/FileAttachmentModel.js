/*
 * A FileAttachmentModel consists of
 * - id (number)
 * - name (string)
 *
 * This represents a single file attachment
 */

class FileAttachmentModel {
  constructor(id, name) {
    this.id = id
    this.name = name
  }
}

function sortFileAttachmentModels(fileOne, fileTwo) {
  return fileOne.id - fileTwo.id
}

export { FileAttachmentModel, sortFileAttachmentModels }
