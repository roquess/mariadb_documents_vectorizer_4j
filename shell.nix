{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    jdk17  # ou jdk11 selon votre préférence
    maven
    perl536Packages.ImageExifTool
    imagemagick
    tesseract
    ffmpeg
  ];

  shellHook = ''
    echo "Java Development Environment ready"
    java -version
    mvn -version
  '';
}
