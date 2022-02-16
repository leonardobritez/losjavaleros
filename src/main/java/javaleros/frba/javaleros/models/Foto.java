package javaleros.frba.javaleros.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Foto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String fileName;

  private String fileType;

  @Lob
  private byte[] data;

  public Foto() {

  }

  public Foto(String fileName, String fileType, byte[] data) {
      this.fileName = fileName;
      this.fileType = fileType;
      this.data = data;
  }

}